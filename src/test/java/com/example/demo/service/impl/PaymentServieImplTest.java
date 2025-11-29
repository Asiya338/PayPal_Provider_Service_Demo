package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.example.demo.http.HttpRequest;
import com.example.demo.http.HttpServiceEngine;
import com.example.demo.pojo.CaptureOrderRes;
import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.pojo.CreateOrderRes;
import com.example.demo.pojo.ShowOrderRes;
import com.example.demo.service.CreateOrderResValidator;
import com.example.demo.service.PaymentValidator;
import com.example.demo.service.TokenService;
import com.example.demo.service.helper.CaptureOrderHelperReq;
import com.example.demo.service.helper.CaptureOrderHelperRes;
import com.example.demo.service.helper.CreateOrderHelperReq;
import com.example.demo.service.helper.CreateOrderHelperRes;
import com.example.demo.service.helper.ShowOrderHelperReq;
import com.example.demo.service.helper.ShowOrderHelperRes;

@ExtendWith(MockitoExtension.class)
public class PaymentServieImplTest {

	@InjectMocks
	private PaymentServiceImpl paymentServiceImpl;

	@Mock
	private PaymentValidator paymentValidator;

	@Mock
	private HttpServiceEngine httpServiceEngine;

	@Mock
	private CreateOrderHelperReq createOrderHelperReq;

	@Mock
	private CreateOrderHelperRes createOrderHelperRes;

	@Mock
	private CaptureOrderHelperRes captureOrderHelperRes;

	@Mock
	private CreateOrderResValidator createOrderResValidator;

	@Mock
	private CaptureOrderHelperReq captureOrderHelperReq;

	@Mock
	private TokenService tokenService;

	@Mock
	private ShowOrderHelperReq showOrderHelperReq;

	@Mock
	private ShowOrderHelperRes showOrderHelperRes;

	@Test
	void createOrder() {
		CreateOrderReq createOrderReq = new CreateOrderReq();
		doNothing().when(paymentValidator).validateService(createOrderReq);

		when(tokenService.getAccessToken()).thenReturn("accessToken");

		HttpRequest httpRequest = null;
		when(createOrderHelperReq.prepareCreateOrderReq(createOrderReq, "accessToken")).thenReturn(httpRequest);

		ResponseEntity<String> httpResponse = null;
		when(httpServiceEngine.makeHttpCall(httpRequest)).thenReturn(httpResponse);

		CreateOrderRes createOrderRes = new CreateOrderRes("ORD_1234", "PAYER_ACTION_REQUIRED",
				"https://paypal.com/redirect");
		when(createOrderHelperRes.handlePaypalResponse(httpResponse)).thenReturn(createOrderRes);

		CreateOrderRes createOrderResponse = paymentServiceImpl.createOrder(createOrderReq);

		assertNotNull(createOrderResponse);
		assertEquals("ORD_1234", createOrderResponse.getOrderId());
		assertEquals("PAYER_ACTION_REQUIRED", createOrderResponse.getPaymentStatus());
		assertEquals("https://paypal.com/redirect", createOrderResponse.getRedirectUrl());

	}

	@Test
	void captureOrderTest() {
		String orderId = "ORD_1234";

		when(tokenService.getAccessToken()).thenReturn("accessToken");

		HttpRequest httpRequest = null;
		when(captureOrderHelperReq.prepareCaptureOrderReq(orderId, "accessToken")).thenReturn(httpRequest);

		ResponseEntity<String> httpResponse = null;
		when(httpServiceEngine.makeHttpCall(httpRequest)).thenReturn(httpResponse);

		CaptureOrderRes captureOrderRes = new CaptureOrderRes("ORD_1234", "PAYER_ACTION_REQUIRED");
		when(captureOrderHelperRes.prepareCaptureResponse(httpResponse)).thenReturn(captureOrderRes);

		CaptureOrderRes captureOrderResponse = paymentServiceImpl.captureOrder(orderId);

		assertNotNull(captureOrderResponse);
		assertEquals("ORD_1234", captureOrderResponse.getOrderId());
		assertEquals("PAYER_ACTION_REQUIRED", captureOrderResponse.getPaymentStatus());

	}

	@Test
	void showOrderTest() {
		String orderId = "ORD_1234";

		when(tokenService.getAccessToken()).thenReturn("accessToken");

		HttpRequest httpRequest = null;
		when(showOrderHelperReq.prepareShowOrderReq(orderId, "accessToken")).thenReturn(httpRequest);

		ResponseEntity<String> httpResponse = null;
		when(httpServiceEngine.makeHttpCall(httpRequest)).thenReturn(httpResponse);

		ShowOrderRes showOrderRes = new ShowOrderRes("ORD_1234", "PAYER_ACTION_REQUIRED");
		when(showOrderHelperRes.prepareShowResponse(httpResponse)).thenReturn(showOrderRes);

		ShowOrderRes showOrderResponse = paymentServiceImpl.showOrder(orderId);

		assertNotNull(showOrderResponse);
		assertEquals("ORD_1234", showOrderResponse.getOrderId());
		assertEquals("PAYER_ACTION_REQUIRED", showOrderResponse.getPaymentStatus());
	}

}
