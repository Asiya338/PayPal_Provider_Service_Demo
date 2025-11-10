package com.example.demo.service.helper;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.example.demo.constant.Constant;
import com.example.demo.http.HttpRequest;
import com.example.demo.paypal.req.Amount;
import com.example.demo.paypal.req.ExperienceContext;
import com.example.demo.paypal.req.OrderRequest;
import com.example.demo.paypal.req.PaymentSource;
import com.example.demo.paypal.req.Paypal;
import com.example.demo.paypal.req.PurchaseUnits;
import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateOrderHelperReq {

	private final JsonUtil jsonUtil;

	@Value("${paypal.create.order.url}")
	public String createOrderUrl;

	public HttpRequest prepareCreateOrderReq(CreateOrderReq createOrderReq, String accessToken) {
		String requestAsJson;

		HttpHeaders headers = prepareHeader(accessToken);

		requestAsJson = prepareReqBodyAsJson(createOrderReq);
		log.info("Create Order Request JSON: \n{}", requestAsJson);

		HttpRequest httpRequest = new HttpRequest();

		httpRequest.setHttpMethod(HttpMethod.POST);
		httpRequest.setUrl(createOrderUrl);
		httpRequest.setBody(requestAsJson);
		httpRequest.setHeaders(headers);

		log.info("Prepared HTTP Request for Create Order : {} ", httpRequest);
		return httpRequest;
	}

	private String prepareReqBodyAsJson(CreateOrderReq createOrderReq) {
		String requestAsJson;
		// Prepare ExperienceContext
		ExperienceContext context = new ExperienceContext();

		context.setPaymentMethodPreference(Constant.IMMEDIATE_PAYMENT_REQUIRED);
		context.setLandingPage(Constant.LANDING_PAGE_LOGIN);
		context.setShippingPreference(Constant.SHIPPING_PREF_NO_SHIPPING);
		context.setUserAction(Constant.USER_ACTION_PAY_NOW);

		context.setReturnUrl(createOrderReq.getReturnUrl());
		context.setCancelUrl(createOrderReq.getCancelUrl());

		// Prepare PayPal + PaymentSource
		Paypal paypal = new Paypal();
		paypal.setExperienceContext(context);

		PaymentSource paymentSource = new PaymentSource();
		paymentSource.setPaypal(paypal);

		// Prepare Amount + PurchaseUnit
		Amount amount = new Amount();
//		amount.setCurrencyCode(createOrderReq.getCurrencyCode());
		String value = String.format(Constant.TWO_DECIMAL_FORMAT, createOrderReq.getAmount());
		amount.setValue(value);

		PurchaseUnits purchaseUnit = new PurchaseUnits();
		purchaseUnit.setAmount(amount);

		// Prepare PayPalOrderRequest
		OrderRequest order = new OrderRequest();
		order.setIntent(Constant.INTENT_CAPTURE);
		order.setPaymentSource(paymentSource);
		order.setPurchaseUnits(List.of(purchaseUnit));

		log.info("create order request object : {}", order);

		// Convert to JSON
		requestAsJson = jsonUtil.toJson(order);
		return requestAsJson;
	}

	private HttpHeaders prepareHeader(String accessToken) {
		HttpHeaders headers = new HttpHeaders();

		String uuid = UUID.randomUUID().toString();
		headers.add(Constant.PAY_PAL_REQUEST_ID, uuid);

		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}