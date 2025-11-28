package com.example.demo.service.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.demo.constant.Constant;
import com.example.demo.http.HttpRequest;
import com.example.demo.paypal.req.OrderRequest;
import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.util.JsonUtil;

@ExtendWith(MockitoExtension.class)
class CreateOrderHelperReqTest {

	@InjectMocks
	private CreateOrderHelperReq createOrderHelperReq;

	@Mock
	private JsonUtil jsonUtil;

	@Test
	void prepareHeader() {
		String accessToken = "accessToken";
		HttpHeaders httpHeaders = createOrderHelperReq.prepareHeader(accessToken);

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
	void prepareCreateOrderReqTest() {
		CreateOrderReq createOrderReq = new CreateOrderReq("USD", 109.90, "https://merchant.returnUrl.com",
				"https://merchant.cancelurl.com");

		String accessToken = "accessToken";

		ReflectionTestUtils.setField(createOrderHelperReq, "createOrderUrl",
				"https://api-m.sandbox.paypal.com/v1/orders");

		HttpRequest httpRequest = createOrderHelperReq.prepareCreateOrderReq(createOrderReq, accessToken);

		assertNotNull(httpRequest);
		assertNotNull(httpRequest.getHeaders());
		assertEquals("https://api-m.sandbox.paypal.com/v1/orders", httpRequest.getUrl());
		assertEquals(HttpMethod.POST, httpRequest.getHttpMethod());
	}

	@Test
	void prepareReqBodyAsJsonTest() {
		CreateOrderReq createOrderReq = new CreateOrderReq("USD", 109.90, "https://merchant.com/.returnUrl",
				"https://merchant.com/cancelurl");

		String requestAsJson = "some response";
		when(jsonUtil.toJson(any(OrderRequest.class))).thenReturn(requestAsJson);

		String resultJson = createOrderHelperReq.prepareReqBodyAsJson(createOrderReq);

		assertNotNull(requestAsJson);
		assertEquals(requestAsJson, resultJson);
	}

}
