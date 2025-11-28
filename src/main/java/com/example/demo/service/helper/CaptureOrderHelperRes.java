package com.example.demo.service.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.constant.ErrorCodeEnum;
import com.example.demo.exception.PayPalProviderException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.paypal.res.capture.CaptureOrderResponse;
import com.example.demo.paypal.res.error.capture.PaypalCaptureErrorRes;
import com.example.demo.pojo.CaptureOrderRes;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.PaypalOrderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaptureOrderHelperRes {
	private final JsonUtil jsonUtil;

	CaptureOrderRes toCaptureResponse(CaptureOrderResponse captureOrderResponse) {
		CaptureOrderRes captureOrderRes = new CaptureOrderRes();
		captureOrderRes.setOrderId(captureOrderResponse.getId());
		captureOrderRes.setPaymentStatus(captureOrderResponse.getStatus());

		return captureOrderRes;
	}

	public CaptureOrderRes prepareCaptureResponse(ResponseEntity<String> httpResponse) {
		log.info("Http Response for Capture Order request : {} ", httpResponse);

		if (httpResponse.getStatusCode().is2xxSuccessful()) {
			CaptureOrderResponse captureOrderResponse = jsonUtil.fromJson(httpResponse.getBody(),
					CaptureOrderResponse.class);
			log.info("Capture Order response from paypal : {} ", captureOrderResponse);

			CaptureOrderRes captureOrderRes = toCaptureResponse(captureOrderResponse);
			log.info("Capture Order request || CaptureOrderRes : {} ", captureOrderRes);

			if (captureOrderResponse != null && captureOrderResponse.getId() != null
					&& captureOrderResponse.getStatus() != null && !captureOrderResponse.getId().isEmpty()
					&& !captureOrderResponse.getStatus().isEmpty()) {

				log.info("Capture Order response with required fields and status COMPLETED : {} ", captureOrderRes);
				return captureOrderRes;
			}

			log.error("Error in capturing order request : || PayPalCaptureOrderResponse  {} ", captureOrderRes);
		}

		if (httpResponse.getStatusCode().is4xxClientError() || httpResponse.getStatusCode().is5xxServerError()) {
			log.error("Recieved 4xx, 5xx error from paypal capture order response ");

			PaypalCaptureErrorRes paypalCaptureErrorRes = jsonUtil.fromJson(httpResponse.getBody(),
					PaypalCaptureErrorRes.class);
			log.error("Paypal capture order error response recieved : {} ", paypalCaptureErrorRes);

			String errorCode = ErrorCodeEnum.RESOURCE_NOT_FOUND.getErrorCode();
			String errorMessage = PaypalOrderUtil.generatePaypalCaptureErrorSummary(paypalCaptureErrorRes);
			log.error("Error Message from paypal : {} ", errorMessage);

			log.error("Error response recieved PaypalCaptureErrorRes || Status Code : {} , Error response  Body : {} ",
					httpResponse.getStatusCode(), httpResponse.getBody());

			throw new ResourceNotFoundException(errorCode, errorMessage,
					HttpStatus.valueOf(httpResponse.getStatusCode().value()));
		}

		throw new PayPalProviderException(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorCode(),
				ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorMessage(), HttpStatus.GATEWAY_TIMEOUT);

	}
}
