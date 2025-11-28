package com.example.demo.service.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.demo.constant.Constant;
import com.example.demo.http.HttpRequest;

@ExtendWith(MockitoExtension.class)
class ShowOrderHelperReqTest {

	@InjectMocks
	private ShowOrderHelperReq showOrderHelperReq;

	@Test
	void prepareCaptureHeaderTest() {

		HttpHeaders httpHeaders = showOrderHelperReq.prepareCaptureHeader("accessToken");

		assertNotNull(httpHeaders);
		assertEquals(MediaType.APPLICATION_JSON, httpHeaders.getContentType());

		assertTrue(httpHeaders.containsKey(Constant.PAY_PAL_REQUEST_ID));
		String requestId = httpHeaders.getFirst(Constant.PAY_PAL_REQUEST_ID);
		assertNotNull(requestId);
		assertEquals(36, requestId.length());

		assertNotNull(httpHeaders.getFirst(HttpHeaders.AUTHORIZATION));
		assertTrue(httpHeaders.getFirst(HttpHeaders.AUTHORIZATION).startsWith("Bearer "));

		assertEquals(3, httpHeaders.size());

	}

	@Test
	void prepareCaptureRequestTest() {
		String orderId = "ORD_1234";
		HttpHeaders httpHeaders = new HttpHeaders();

		ReflectionTestUtils.setField(showOrderHelperReq, "showUrl",
				"https://api-m.sandbox.paypal.com/v1/orders/{orderId}");

		HttpRequest httpRequest = showOrderHelperReq.prepareCaptureRequest(orderId, httpHeaders);

		assertNotNull(httpRequest);

		assertEquals("https://api-m.sandbox.paypal.com/v1/orders/ORD_1234", httpRequest.getUrl());
		assertEquals(HttpMethod.GET, httpRequest.getHttpMethod());
		assertSame(httpHeaders, httpRequest.getHeaders()); // same object reference
		assertNotNull(httpRequest.getBody());
	}

	@Test
	void prepareCaptureOrderReqTest() {
		String orderId = "ORD_1234";

		ReflectionTestUtils.setField(showOrderHelperReq, "showUrl",
				"https://api-m.sandbox.paypal.com/v1/orders/{orderId}");

		HttpRequest httpRequest = showOrderHelperReq.prepareCaptureOrderReq(orderId, "accessToken");

		assertNotNull(httpRequest);
		assertNotNull(httpRequest.getBody());
		assertNotNull(httpRequest.getHeaders());

		assertEquals("https://api-m.sandbox.paypal.com/v1/orders/ORD_1234", httpRequest.getUrl());
		assertEquals(HttpMethod.GET, httpRequest.getHttpMethod());

	}

}
