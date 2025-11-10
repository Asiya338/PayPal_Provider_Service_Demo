package com.example.demo.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.http.HttpRequest;
import com.example.demo.http.HttpServiceEngine;
import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.pojo.CreateOrderRes;
import com.example.demo.service.PaymentValidator;
import com.example.demo.service.TokenService;
import com.example.demo.service.helper.CreateOrderHelperReq;
import com.example.demo.service.helper.CreateOrderHelperRes;
import com.example.demo.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	private final PaymentValidator paymentValidator;
	private final HttpServiceEngine httpServiceEngine;
	private final CreateOrderHelperReq createOrderHelperReq;
	private final CreateOrderHelperRes createOrderHelperRes;

	private final TokenService tokenService;

	@Override
	public CreateOrderRes createOrder(CreateOrderReq createOrderReq) {
		paymentValidator.validateService(createOrderReq);
		log.info("Create order request in payment service impl || CreateOrderReq : {} ", createOrderReq);

		String accessToken = tokenService.getAccessToken();
		log.info("access_token from token service : {} ", accessToken);

		HttpRequest httpRequest = createOrderHelperReq.prepareCreateOrderReq(createOrderReq, accessToken);

		ResponseEntity<String> httpResponse = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("HTTP Response from Create Order : {}  ", httpResponse);

		CreateOrderRes createOrderRes = createOrderHelperRes.handlePaypalResponse(httpResponse);
		log.info("Create order response  || CreateOrderRes : {}", createOrderRes);

		return createOrderRes;
	}
}
