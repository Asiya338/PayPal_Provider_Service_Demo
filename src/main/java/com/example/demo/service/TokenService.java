package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
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

//	private final RedisService redisService;
	private final JsonUtil jsonUtil;
	private final HttpServiceEngine httpServiceEngine;

	@Value("${paypal.client.id}")
	private String clientId;

	@Value("${paypal.client.secret}")
	private String clientSecret;

	@Value("${paypal.oauth.url}")
	private String oauthUrl;

//	@Cacheable(value = "paypalToken") // name of redis to store accessToken
	public String getAccessToken() {

		log.trace("tracing");
		log.debug("debugging");
		log.info("info");
		log.warn("wraning");
		log.error("error ");

		log.info("getting access token from TOKEN SERVICE");

//		String accessToken = redisService.getValue(Constant.PAYPAL_ACCESS_TOKEN);
		String accessToken = null;
		if (accessToken != null) {
			log.info("Getting Access Token from redis cache... ");

			return accessToken;
		}

		log.info("No accessToken found in redis, making OAuth api call to PayPal...");

		HttpHeaders headers = prepareHttpHeader();

		HttpRequest httpRequest = prepareHttpRequest(headers);

		ResponseEntity<String> httpResponse = httpServiceEngine.makeHttpCall(httpRequest);

		PaypalOAuthToken token = jsonUtil.fromJson(httpResponse.getBody(), PaypalOAuthToken.class);

		accessToken = token.getAccessToken();
//		redisService.setValueWithExpiry(Constant.PAYPAL_ACCESS_TOKEN, accessToken,
//				token.getExpiresIn() - Constant.PAYPAL_ACCESS_TOKEN_EXPIRY_DIFF);

		log.info("New access token generated and stored in Redis");

		return accessToken;
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
