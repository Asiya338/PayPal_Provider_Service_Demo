package com.example.demo.service.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.constant.ErrorCodeEnum;
import com.example.demo.exception.PayPalProviderException;
import com.example.demo.paypal.res.create.PaypalLinks;
import com.example.demo.paypal.res.create.PaypalOrder;
import com.example.demo.paypal.res.error.create.PayPalErrorResponse;
import com.example.demo.pojo.CreateOrderRes;
import com.example.demo.util.JsonUtil;

@ExtendWith(MockitoExtension.class)
class CreateOrderHelperResTest {

	@InjectMocks
	private CreateOrderHelperRes createOrderHelperRes;

	@Mock
	private JsonUtil jsonUtil;

	@Test
	void toOrderResponseTest() {
		PaypalLinks selfLink = new PaypalLinks();
		selfLink.setRel("self");
		selfLink.setHref("https://api.sandbox.paypal.com/v2/checkout/orders/123");

		PaypalLinks payerActionLink = new PaypalLinks();
		payerActionLink.setRel("payer-action");
		payerActionLink.setHref("https://www.sandbox.paypal.com/checkoutnow?token=123");

		List<PaypalLinks> links = List.of(selfLink, payerActionLink);
		PaypalOrder paypalOrderRes = new PaypalOrder("123", "PAYER_ACTION_REQUIRED", null, links);

		CreateOrderRes createOrderRes = createOrderHelperRes.toOrderResponse(paypalOrderRes);

		assertNotNull(createOrderRes);
		assertNotNull(createOrderRes.getOrderId());
		assertNotNull(createOrderRes.getPaymentStatus());
	}

	@Test
	void prepareCreateResponseTest_2xxSuccess() {
		ResponseEntity<String> httpResponse = ResponseEntity.ok("success");

		PaypalLinks selfLink = new PaypalLinks();
		selfLink.setRel("self");
		selfLink.setHref("https://api.sandbox.paypal.com/v2/checkout/orders/123");

		PaypalLinks payerActionLink = new PaypalLinks();
		payerActionLink.setRel("payer-action");
		payerActionLink.setHref("https://www.sandbox.paypal.com/checkoutnow?token=123");

		List<PaypalLinks> links = List.of(selfLink, payerActionLink);

		PaypalOrder paypalOrderRes = new PaypalOrder("123", "PAYER_ACTION_REQUIRED", null, links);

		when(jsonUtil.fromJson(httpResponse.getBody(), PaypalOrder.class)).thenReturn(paypalOrderRes);

		CreateOrderRes CreateOrderRes = createOrderHelperRes.handlePaypalResponse(httpResponse);

		assertNotNull(CreateOrderRes);
		assertNotNull(CreateOrderRes.getOrderId());
		assertNotNull(CreateOrderRes.getPaymentStatus());
	}

	@Test
	void prepareCreateResponseTest_4xx_5xx_Failure() {
		ResponseEntity<String> httpResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		PayPalErrorResponse paypalErrorRes = new PayPalErrorResponse(null, null, null,
				"4xx | 5xx error occured while creating order... ", null, null, null);
		when(jsonUtil.fromJson(httpResponse.getBody(), PayPalErrorResponse.class)).thenReturn(paypalErrorRes);

		PayPalProviderException ex = assertThrows(PayPalProviderException.class,
				() -> createOrderHelperRes.handlePaypalResponse(httpResponse));

		assertNotNull(paypalErrorRes);
		assertEquals(ErrorCodeEnum.PAYPAL_ERROR.getErrorCode(), ex.getErrorCode());
		assertEquals("Message: 4xx | 5xx error occured while creating order... ", ex.getErrorMessage());
		assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(httpResponse.getStatusCode().value()));

	}

	@Test
	void prepareCreateResponseTest_unExpected_Failure() {

		// 2xx HTTP Status but invalid JSON fields → leads to UNKNOWN_ERROR
		ResponseEntity<String> httpResponse = ResponseEntity.ok("invalid-response");

		// Order missing required fields
		PaypalOrder paypalOrderRes = new PaypalOrder("908", null, null, null);

		when(jsonUtil.fromJson("invalid-response", PaypalOrder.class)).thenReturn(paypalOrderRes);

		PayPalProviderException ex = assertThrows(PayPalProviderException.class,
				() -> createOrderHelperRes.handlePaypalResponse(httpResponse));

		assertEquals(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.PAYPAL_UNKNOWN_ERROR.getErrorMessage(), ex.getErrorMessage());
		assertEquals(HttpStatus.GATEWAY_TIMEOUT, ex.getHttpStatus());
	}

}
