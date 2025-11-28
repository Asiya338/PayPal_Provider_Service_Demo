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
import com.example.demo.paypal.res.error.show.PaypalShowErrorRes;
import com.example.demo.paypal.res.show.ShowOrderResponse;
import com.example.demo.pojo.ShowOrderRes;
import com.example.demo.util.JsonUtil;

@ExtendWith(MockitoExtension.class)
public class ShowOrderHelperResTest {

	@InjectMocks
	private ShowOrderHelperRes showOrderHelperRes;

	@Mock
	private JsonUtil jsonUtil;

	@Test
	void toShowResponseTest() {
		ShowOrderResponse showOrderResponse = new ShowOrderResponse("ORD_1234", null, "APPROVED", null, null, null,
				null);

		ShowOrderRes showOrderRes = showOrderHelperRes.toShowResponse(showOrderResponse);

		assertNotNull(showOrderRes);
		assertNotNull(showOrderRes.getOrderId());
		assertNotNull(showOrderRes.getPaymentStatus());
	}

	@Test
	void prepareShowResponseTest_2xxSuccess() {
		ResponseEntity<String> httpResponse = ResponseEntity.ok("success");

		ShowOrderResponse showOrderResponse = new ShowOrderResponse("123", null, "AAPROVED", null, null, null, null);

		when(jsonUtil.fromJson(httpResponse.getBody(), ShowOrderResponse.class)).thenReturn(showOrderResponse);

		ShowOrderRes showOrderRes = showOrderHelperRes.prepareShowResponse(httpResponse);

		assertNotNull(showOrderRes);
		assertNotNull(showOrderRes.getOrderId());
		assertNotNull(showOrderRes.getPaymentStatus());
	}

	@Test
	void prepareShowResponseTest_4xx_5xx_Failure() {
		ResponseEntity<String> httpResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		PaypalShowErrorRes paypalShowErrorRes = new PaypalShowErrorRes(null, null,
				"4xx | 5xx error occured while fetching order... ", null, null);
		when(jsonUtil.fromJson(httpResponse.getBody(), PaypalShowErrorRes.class)).thenReturn(paypalShowErrorRes);

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
				() -> showOrderHelperRes.prepareShowResponse(httpResponse));

		assertNotNull(paypalShowErrorRes);
		assertEquals(ErrorCodeEnum.RESOURCE_NOT_FOUND.getErrorCode(), ex.getErrorCode());
		assertEquals("Message: 4xx | 5xx error occured while fetching order... ", ex.getErrorMessage());
		assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(httpResponse.getStatusCode().value()));

	}

	@Test
	void prepareShowResponseTest_unExpected_Failure() {
		ResponseEntity<String> httpResponse = ResponseEntity.ok("invalid-response");

		ShowOrderResponse showOrderResponse = new ShowOrderResponse(null, null, null, null, null, null, null);

		when(jsonUtil.fromJson(httpResponse.getBody(), ShowOrderResponse.class)).thenReturn(showOrderResponse);

		PayPalProviderException ex = assertThrows(PayPalProviderException.class,
				() -> showOrderHelperRes.prepareShowResponse(httpResponse));

		assertEquals(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorMessage(), ex.getErrorMessage());
		assertEquals(HttpStatus.GATEWAY_TIMEOUT, ex.getHttpStatus());

	}
}
