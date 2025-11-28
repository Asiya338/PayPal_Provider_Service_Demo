package com.example.demo.service.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.constant.ErrorCodeEnum;
import com.example.demo.exception.PayPalProviderException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.paypal.res.capture.CaptureOrderResponse;
import com.example.demo.paypal.res.error.capture.PaypalCaptureErrorRes;
import com.example.demo.pojo.CaptureOrderRes;
import com.example.demo.util.JsonUtil;

@ExtendWith(MockitoExtension.class)
class CaptureOrderHelperResTest {

	@Mock
	private JsonUtil jsonUtil;

	@InjectMocks
	private CaptureOrderHelperRes captureOrderHelperRes;

	@Test
	void toCaptureResponseTest() {
		CaptureOrderResponse captureOrderResponse = new CaptureOrderResponse("123", "PAYER_ACTION_REQUIRED", null, null,
				null, null);

		CaptureOrderRes captureOrderRes = captureOrderHelperRes.toCaptureResponse(captureOrderResponse);

		assertNotNull(captureOrderRes);
		assertNotNull(captureOrderRes.getOrderId());
		assertNotNull(captureOrderRes.getPaymentStatus());
	}

	@Test
	void prepareCaptureResponseTest_2xxSuccess() {
		ResponseEntity<String> httpResponse = ResponseEntity.ok("success");

		CaptureOrderResponse captureOrderResponse = new CaptureOrderResponse("123", "PAYER_ACTION_REQUIRED", null, null,
				null, null);

		when(jsonUtil.fromJson(httpResponse.getBody(), CaptureOrderResponse.class)).thenReturn(captureOrderResponse);

		CaptureOrderRes captureOrderRes = captureOrderHelperRes.prepareCaptureResponse(httpResponse);

		assertNotNull(captureOrderRes);
		assertNotNull(captureOrderRes.getOrderId());
		assertNotNull(captureOrderRes.getPaymentStatus());
	}

	@Test
	void prepareCaptureResponseTest_4xx_5xx_Failure() {
		ResponseEntity<String> httpResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		PaypalCaptureErrorRes paypalCaptureErrorRes = new PaypalCaptureErrorRes(null, null,
				"4xx | 5xx error occured while capturing order... ", null, null);
		when(jsonUtil.fromJson(httpResponse.getBody(), PaypalCaptureErrorRes.class)).thenReturn(paypalCaptureErrorRes);

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
				() -> captureOrderHelperRes.prepareCaptureResponse(httpResponse));

		assertNotNull(paypalCaptureErrorRes);
		assertEquals(ErrorCodeEnum.RESOURCE_NOT_FOUND.getErrorCode(), ex.getErrorCode());
		assertEquals("Message: 4xx | 5xx error occured while capturing order... ", ex.getErrorMessage());
		assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(httpResponse.getStatusCode().value()));

	}

	@Test
	void prepareCaptureResponseTest_unExpected_Failure() {
		ResponseEntity<String> httpResponse = ResponseEntity.ok("invalid-response");

		CaptureOrderResponse captureOrderResponse = new CaptureOrderResponse(null, null, null, null, null, null);

		when(jsonUtil.fromJson(httpResponse.getBody(), CaptureOrderResponse.class)).thenReturn(captureOrderResponse);

		PayPalProviderException ex = assertThrows(PayPalProviderException.class,
				() -> captureOrderHelperRes.prepareCaptureResponse(httpResponse));

		assertEquals(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorMessage(), ex.getErrorMessage());
		assertEquals(HttpStatus.GATEWAY_TIMEOUT, ex.getHttpStatus());

	}
}
