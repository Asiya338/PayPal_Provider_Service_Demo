package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.constant.Constant;
import com.example.demo.http.HttpRequest;
import com.example.demo.http.HttpServiceEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

	private final HttpServiceEngine httpServiceEngine;

	private static String accessToken;

	@Value("${paypal.client.id}")
	private String clientId;

	@Value("${paypal.client.secret}")
	private String clientSecret;

	@Value("${paypal.oauth.url}")
	private String oauthUrl;

	public String getAccessToken() {
		log.info("getting access token from token service");

		if (accessToken != null) {
			log.info("returning cached access token");
			return accessToken;
		}

		log.info("No cached access token found, calling oauth service");

		log.info("make http request called in http service engine");

		HttpHeaders headers = new HttpHeaders();

		headers.setBasicAuth(clientId, clientSecret);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> bodyValue = new LinkedMultiValueMap<>();

		bodyValue.add(Constant.GRANT_TYPE, Constant.CLIENT_CREDENTIALS);

		HttpRequest httpRequest = new HttpRequest();

		httpRequest.setUrl(oauthUrl);
		httpRequest.setHttpMethod(HttpMethod.POST);
		httpRequest.setHeaders(headers);
		httpRequest.setBody(bodyValue);

		log.info("Http request : {} ", httpRequest);

		return httpServiceEngine.makeHttpCall(httpRequest).getBody();

	}
}
