package com.example.demo.http;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpServiceEngine {

	private final RestClient restClient;

	public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {

		try {
			ResponseEntity<String> httpResponse = restClient.method(httpRequest.getHttpMethod())
					.uri(httpRequest.getUrl())
					.headers(restClientHeader -> restClientHeader.addAll(httpRequest.getHeaders()))
					.body(httpRequest.getBody()).retrieve().toEntity(String.class);
			log.info("Http response : {} ", httpResponse);

			return httpResponse;
		} catch (Exception e) {
			log.error("Error while making http api request from HttpServiceEngine | : {} ", e.getMessage());
			throw new RuntimeException("Exception occured in HttpServiceEngine");
		}

	}
}