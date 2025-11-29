package com.example.demo.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.http.HttpRequest;
import com.example.demo.http.HttpServiceEngine;
import com.example.demo.pojo.CaptureOrderRes;
import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.pojo.CreateOrderRes;
import com.example.demo.pojo.ShowOrderRes;
import com.example.demo.service.CreateOrderResValidator;
import com.example.demo.service.PaymentValidator;
import com.example.demo.service.TokenService;
import com.example.demo.service.helper.CaptureOrderHelperReq;
import com.example.demo.service.helper.CaptureOrderHelperRes;
import com.example.demo.service.helper.CreateOrderHelperReq;
import com.example.demo.service.helper.CreateOrderHelperRes;
import com.example.demo.service.helper.ShowOrderHelperReq;
import com.example.demo.service.helper.ShowOrderHelperRes;
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
	private final CaptureOrderHelperRes captureOrderHelperRes;
	private final CreateOrderResValidator createOrderResValidator;
	private final CaptureOrderHelperReq captureOrderHelperReq;
	private final TokenService tokenService;
	private final ShowOrderHelperReq showOrderHelperReq;
	private final ShowOrderHelperRes showOrderHelperRes;

	private static CreateOrderRes createOrderRes;
	private static String accessToken;

	@Override
	public CreateOrderRes createOrder(CreateOrderReq createOrderReq) {
		paymentValidator.validateService(createOrderReq);
		log.info("Create order request in payment service impl || CreateOrderReq : {} ", createOrderReq);

		accessToken = tokenService.getAccessToken();
		log.info("access_token from token service ");

		HttpRequest httpRequest = createOrderHelperReq.prepareCreateOrderReq(createOrderReq, accessToken);

		ResponseEntity<String> httpResponse = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("HTTP Response from Create Order : {}  ", httpResponse);

		createOrderRes = createOrderHelperRes.handlePaypalResponse(httpResponse);
		log.info("Create order response  || CreateOrderRes : {}", createOrderRes);

		return createOrderRes;
	}

	@Override
	public CaptureOrderRes captureOrder(String orderId) {
		log.info("order Id in Capture Order request : {} ", orderId);

//		createOrderResValidator.validateOrderRes(createOrderRes);

		accessToken = tokenService.getAccessToken();
		log.info("access_token from token service ");

		ResponseEntity<String> httpResponse = httpServiceEngine
				.makeHttpCall(captureOrderHelperReq.prepareCaptureOrderReq(orderId, accessToken));
		log.info("Http Response for Capture Order : {} ", httpResponse);

		CaptureOrderRes captureOrderRes = captureOrderHelperRes.prepareCaptureResponse(httpResponse);
		log.info("Capture Order Response || CaptureOrderRes : {}  ", captureOrderRes);

		return captureOrderRes;
	}

	@Override
	public ShowOrderRes showOrder(String orderId) {
		log.info("order Id in SHOW Order request : {} ", orderId);

		accessToken = tokenService.getAccessToken();
		log.info("access_token from token service ");

		ResponseEntity<String> httpResponse = httpServiceEngine
				.makeHttpCall(showOrderHelperReq.prepareShowOrderReq(orderId, accessToken));
		log.info("Http Response for Show Order : {} ", httpResponse);

		ShowOrderRes showOrderRes = showOrderHelperRes.prepareShowResponse(httpResponse);
		log.info("Show Order Response || ShowOrderRes : {}  ", showOrderRes);

		return showOrderRes;
	}
}
