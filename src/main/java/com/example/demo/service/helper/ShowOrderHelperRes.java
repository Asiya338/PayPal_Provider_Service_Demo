package com.example.demo.service.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.constant.ErrorCodeEnum;
import com.example.demo.exception.PayPalProviderException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.paypal.res.error.show.PaypalShowErrorRes;
import com.example.demo.paypal.res.show.ShowOrderResponse;
import com.example.demo.pojo.ShowOrderRes;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.PaypalOrderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowOrderHelperRes {
	private final JsonUtil jsonUtil;

	ShowOrderRes toShowResponse(ShowOrderResponse showOrderResponse) {
		ShowOrderRes showOrderRes = new ShowOrderRes();
		showOrderRes.setOrderId(showOrderResponse.getId());
		showOrderRes.setPaymentStatus(showOrderResponse.getStatus());

		return showOrderRes;
	}

	public ShowOrderRes prepareShowResponse(ResponseEntity<String> httpResponse) {
		log.info("Http Response for Show Order request : {} ", httpResponse);

		if (httpResponse.getStatusCode().is2xxSuccessful()) {
			ShowOrderResponse showOrderResponse = jsonUtil.fromJson(httpResponse.getBody(), ShowOrderResponse.class);
			log.info("Show Order response from paypal : {} ", showOrderResponse);

			ShowOrderRes showOrderRes = toShowResponse(showOrderResponse);
			log.info("Show Order request || showOrderRes : {} ", showOrderRes);

			if (showOrderResponse != null && showOrderResponse.getId() != null && showOrderResponse.getStatus() != null
					&& !showOrderResponse.getId().isEmpty() && !showOrderResponse.getStatus().isEmpty()) {

				log.info("Show Order response with required fields  : {} ", showOrderRes);
				return showOrderRes;
			}

			log.error("Error in showing order request : || PayPalShowOrderResponse  {} ", showOrderRes);
		}

		if (httpResponse.getStatusCode().is4xxClientError() || httpResponse.getStatusCode().is5xxServerError()) {
			log.error("Recieved 4xx, 5xx error from paypal show order response ");

			PaypalShowErrorRes paypalShowErrorRes = jsonUtil.fromJson(httpResponse.getBody(), PaypalShowErrorRes.class);
			log.error("Paypal showing order error response recieved : {} ", paypalShowErrorRes);

			String errorCode = ErrorCodeEnum.RESOURCE_NOT_FOUND.getErrorCode();
			String errorMessage = PaypalOrderUtil.generatePaypalShowErrorSummary(paypalShowErrorRes);
			log.error("Error Message from paypal : {} ", errorMessage);

			log.error("Error response recieved PaypaShowErrorRes || Status Code : {} , Error response  Body : {} ",
					httpResponse.getStatusCode(), httpResponse.getBody());

			throw new ResourceNotFoundException(errorCode, errorMessage,
					HttpStatus.valueOf(httpResponse.getStatusCode().value()));
		}

		throw new PayPalProviderException(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorCode(),
				ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorMessage(), HttpStatus.GATEWAY_TIMEOUT);

	}
}
