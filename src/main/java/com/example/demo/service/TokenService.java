package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.constant.Constant;
import com.example.demo.http.HttpRequest;
import com.example.demo.http.HttpServiceEngine;
import com.example.demo.paypal.res.create.PaypalOAuthToken;
import com.example.demo.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

	private final JsonUtil jsonUtil;
	private final HttpServiceEngine httpServiceEngine;

	@Value("${paypal.client.id}")
	private String clientId;

	@Value("${paypal.client.secret}")
	private String clientSecret;

	@Value("${paypal.oauth.url}")
	private String oauthUrl;

	@Cacheable(value = "paypalToken") // name of redis to store accessToken
	public String getAccessToken() {
		log.info("getting access token from TOKEN SERVICE and store it in Redis Cache...");

		HttpHeaders headers = prepareHttpHeader();

		HttpRequest httpRequest = prepareHttpRequest(headers);

		ResponseEntity<String> httpResponse = httpServiceEngine.makeHttpCall(httpRequest);

		PaypalOAuthToken token = jsonUtil.fromJson(httpResponse.getBody(), PaypalOAuthToken.class);
		log.info("TOKEN SERVICE 1st api call to get access token : {} ", token.getAccessToken());

		return token.getAccessToken();
	}

	private HttpRequest prepareHttpRequest(HttpHeaders headers) {
		HttpRequest httpRequest = new HttpRequest();

		MultiValueMap<String, String> bodyValue = new LinkedMultiValueMap<>();

		bodyValue.add(Constant.GRANT_TYPE, Constant.CLIENT_CREDENTIALS);
		httpRequest.setUrl(oauthUrl);
		httpRequest.setHttpMethod(HttpMethod.POST);
		httpRequest.setHeaders(headers);
		httpRequest.setBody(bodyValue);
		log.info("Http request : {} ", httpRequest);
		return httpRequest;
	}

	private HttpHeaders prepareHttpHeader() {
		HttpHeaders headers = new HttpHeaders();

		headers.setBasicAuth(clientId, clientSecret);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headers;
	}
}
