package com.example.demo.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.http.HttpRequest;
import com.example.demo.http.HttpServiceEngine;
import com.example.demo.paypal.res.PaypalOrder;
import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.pojo.CreateOrderRes;
import com.example.demo.service.TokenService;
import com.example.demo.service.helper.CreateOrderHelper;
import com.example.demo.service.interfaces.PaymentService;
import com.example.demo.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	private final JsonUtil jsonUtil;
	private final HttpServiceEngine httpServiceEngine;
	private final CreateOrderHelper createOrderHelper;
	private final TokenService tokenService;

	@Override
	public CreateOrderRes createOrder(CreateOrderReq createOrderReq) {
		String accessToken = tokenService.getAccessToken();
		log.info("access_token from token service : {} ", accessToken);

		HttpRequest httpRequest = createOrderHelper.prepareCreateOrderReq(createOrderReq, accessToken);

		ResponseEntity<String> httpResponse = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("HTTP Response from Create Order : {}  ", httpResponse);

		PaypalOrder paypalOrderRes = jsonUtil.fromJson(httpResponse.getBody(), PaypalOrder.class);

		CreateOrderRes createOrderRes = createOrderHelper.toOrderResponse(paypalOrderRes);
		log.info("Create order response  || CreateOrderRes : {}", createOrderRes);

		return createOrderRes;
	}
}
