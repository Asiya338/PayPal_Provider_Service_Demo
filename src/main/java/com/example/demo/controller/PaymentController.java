package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.pojo.CreateOrderRes;
import com.example.demo.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/payments")
	public CreateOrderRes createOrder(@RequestBody CreateOrderReq createOrderReq) {
		log.info("Create order request in paypal || CreateOrderReq : {} ", createOrderReq);

		CreateOrderRes response = paymentService.createOrder(createOrderReq);
		log.info("response from payment service : {} ", response);

		return response;
	}

}
