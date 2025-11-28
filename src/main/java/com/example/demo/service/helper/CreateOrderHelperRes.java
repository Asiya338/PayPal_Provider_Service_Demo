package com.example.demo.service.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.constant.Constant;
import com.example.demo.constant.ErrorCodeEnum;
import com.example.demo.exception.PayPalProviderException;
import com.example.demo.paypal.res.create.PaypalLinks;
import com.example.demo.paypal.res.create.PaypalOrder;
import com.example.demo.paypal.res.error.create.PayPalErrorResponse;
import com.example.demo.pojo.CreateOrderRes;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.PaypalOrderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateOrderHelperRes {
	private final JsonUtil jsonUtil;

	public CreateOrderRes toOrderResponse(PaypalOrder paypalOrderRes) {
		CreateOrderRes createOrderRes = new CreateOrderRes();
		createOrderRes.setOrderId(paypalOrderRes.getId());
		createOrderRes.setPaymentStatus(paypalOrderRes.getStatus());

		String redirectUrl = paypalOrderRes.getLinks().stream()
				.filter(link -> "payer-action".equalsIgnoreCase(link.getRel())).findFirst().map(PaypalLinks::getHref)
				.orElse("");

		createOrderRes.setRedirectUrl(redirectUrl);

		return createOrderRes;
	}

	public CreateOrderRes handlePaypalResponse(ResponseEntity<String> httpResponse) {
		log.info("Handling PayPal response in PayPalServiceImpl  || httpResponse : {}", httpResponse);

		if (httpResponse.getStatusCode().is2xxSuccessful()) {
			try {
				PaypalOrder paypalOrderRes = jsonUtil.fromJson(httpResponse.getBody(), PaypalOrder.class);

				CreateOrderRes createOrderRes = toOrderResponse(paypalOrderRes);

				if (createOrderRes != null && createOrderRes.getOrderId() != null
						&& !createOrderRes.getOrderId().isEmpty() && createOrderRes.getPaymentStatus() != null
						&& createOrderRes.getPaymentStatus().equalsIgnoreCase(Constant.PAYER_ACTION_REQUIRED)
						&& createOrderRes.getRedirectUrl() != null && !createOrderRes.getRedirectUrl().isEmpty()) {

					return createOrderRes;
				}

			} catch (Exception e) {
				throw new PayPalProviderException(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorCode(),
						ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorMessage(), HttpStatus.GATEWAY_TIMEOUT);
			}
		}

		if (httpResponse.getStatusCode().is4xxClientError() || httpResponse.getStatusCode().is5xxServerError()) {
			log.error("Recieved 4xx, 5xx Error response from Paypal service");

			PayPalErrorResponse paypalErrorRes = jsonUtil.fromJson(httpResponse.getBody(), PayPalErrorResponse.class);
			log.error("Converted Response Body to PaypalOrder : {}", paypalErrorRes);

			String errorCode = ErrorCodeEnum.PAYPAL_ERROR.getErrorCode();
			String errorMessage = PaypalOrderUtil.generatePaypalErrorSummary(paypalErrorRes);
			log.info("Error message from paypal error response  :  {}  ", errorMessage);
			log.error("Error response recieved form Paypal service|| Status Code: {} , Response Body : {}  ",
					httpResponse.getStatusCode(), httpResponse.getBody());

			throw new PayPalProviderException(errorCode, errorMessage,
					HttpStatus.valueOf(httpResponse.getStatusCode().value()));

		}

		throw new PayPalProviderException(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorCode(),
				ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorMessage(), HttpStatus.GATEWAY_TIMEOUT);
	}

}
