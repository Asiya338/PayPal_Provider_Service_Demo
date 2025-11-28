
package com.example.demo.service.helper;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.example.demo.constant.Constant;
import com.example.demo.http.HttpRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShowOrderHelperReq {
	@Value("${paypal.show.order.url}")
	private String showUrl;

	public HttpRequest prepareCaptureOrderReq(String orderId, String accessToken) {
		HttpHeaders httpHeaders = prepareCaptureHeader(accessToken);

		HttpRequest httpRequest = prepareCaptureRequest(orderId, httpHeaders);
		log.info("http request for show order : {} ", httpRequest);

		return httpRequest;
	}

	HttpRequest prepareCaptureRequest(String orderId, HttpHeaders httpHeaders) {
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setHttpMethod(HttpMethod.GET);
		httpRequest.setUrl(showUrl.replace(Constant.ORDER_ID, orderId));
		httpRequest.setBody("");
		httpRequest.setHeaders(httpHeaders);

		return httpRequest;
	}

	HttpHeaders prepareCaptureHeader(String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBearerAuth(accessToken);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		String uuid = UUID.randomUUID().toString();
		httpHeaders.add(Constant.PAY_PAL_REQUEST_ID, uuid);

		return httpHeaders;
	}

}
