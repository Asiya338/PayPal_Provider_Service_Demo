package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.demo.constant.Constant;
import com.example.demo.http.HttpRequest;
import com.example.demo.http.HttpServiceEngine;
import com.example.demo.paypal.res.create.PaypalOAuthToken;
import com.example.demo.util.JsonUtil;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

	@Mock
	private RedisService redisService;

	@Mock
	private JsonUtil jsonUtil;

	@Mock
	private HttpServiceEngine httpServiceEngine;

	@InjectMocks
	private TokenService tokenService;

	@Test
	void prepareHttpRequestTest() {
		HttpHeaders headers = new HttpHeaders();

		ReflectionTestUtils.setField(tokenService, "oauthUrl", "https://api-m.sandbox.paypal.com/v1/oauth2/token");

		HttpRequest httpRequest = tokenService.prepareHttpRequest(headers);

		assertNotNull(httpRequest);
		assert httpRequest.getUrl().equals("https://api-m.sandbox.paypal.com/v1/oauth2/token");
		assert httpRequest.getHttpMethod().name().equals("POST");
		assert httpRequest.getHeaders() == headers;
		assert httpRequest.getBody() != null;
	}

	@Test
	void prepareHttpHeaderTest() {

		ReflectionTestUtils.setField(tokenService, "clientId", "clientId");

		ReflectionTestUtils.setField(tokenService, "clientSecret", "clientSecret");

		HttpHeaders headers = tokenService.prepareHttpHeader();

		assertNotNull(headers);
		assert headers.getContentType().equals(MediaType.APPLICATION_FORM_URLENCODED);
		assert headers.getFirst(HttpHeaders.AUTHORIZATION).startsWith("Basic ");
		assert headers.size() == 2;
	}

	@Test
	void getAccessTokenTest_RC() {
		when(redisService.getValue(Constant.PAYPAL_ACCESS_TOKEN)).thenReturn("accessToken");

		String accessToken = tokenService.getAccessToken();

		assertNotNull(accessToken);
	}

	@Test
	void getAccessTokenTest_PP_OAuth() {
		ReflectionTestUtils.setField(tokenService, "clientId", "dummy-client-id");
		ReflectionTestUtils.setField(tokenService, "clientSecret", "dummy-client-secret");
		ReflectionTestUtils.setField(tokenService, "oauthUrl", "https://api.sandbox.paypal.com/oauth2/token");

		when(redisService.getValue(Constant.PAYPAL_ACCESS_TOKEN)).thenReturn(null);

		ResponseEntity<String> httpResponse = ResponseEntity.ok("SUCCESS");
		when(httpServiceEngine.makeHttpCall(any(HttpRequest.class))).thenReturn(httpResponse);

		PaypalOAuthToken token = new PaypalOAuthToken("accessToken123", 3600);
		when(jsonUtil.fromJson(httpResponse.getBody(), PaypalOAuthToken.class)).thenReturn(token);

		String result = tokenService.getAccessToken();

		assertNotNull(result);
		assertEquals("accessToken123", result);

		verify(redisService).getValue(Constant.PAYPAL_ACCESS_TOKEN);
		verify(httpServiceEngine).makeHttpCall(any(HttpRequest.class));
		verify(jsonUtil).fromJson("SUCCESS", PaypalOAuthToken.class);
	}

}
