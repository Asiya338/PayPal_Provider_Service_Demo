package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.pojo.CaptureOrderRes;
import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.pojo.CreateOrderRes;
import com.example.demo.pojo.ShowOrderRes;
import com.example.demo.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/orders")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentService paymentService;

	@PostMapping
	public CreateOrderRes createOrder(@RequestBody CreateOrderReq createOrderReq) {
		log.info("Create order request in paypal || CreateOrderReq : {} ", createOrderReq);

		CreateOrderRes response = paymentService.createOrder(createOrderReq);
		log.info("response from payment service : {} ", response);

		return response;
	}

	@PostMapping("/{orderId}/capture")
	public CaptureOrderRes captureOrder(@PathVariable String orderId) {
		log.info("Order ID to be captured : {}", orderId);

		CaptureOrderRes response = paymentService.captureOrder(orderId);
		log.info("Response from payment service || CAPTURE ORDER RESPONSE : {} ", response);

		return response;
	}

	@GetMapping("/{orderId}")
	public ShowOrderRes showOrder(@PathVariable String orderId) {
		log.info("Show details of Order ID : {}", orderId);

		ShowOrderRes response = paymentService.showOrder(orderId);
		log.info("Response from payment service || SHOW ORDER RESPONSE : {} ", response);

		return response;
	}

}
