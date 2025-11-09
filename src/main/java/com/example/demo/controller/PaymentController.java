package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.interfaces.PaymentService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/payments")
	public ResponseEntity<String> createOrder() {
		log.info("inside payments controller");

		ResponseEntity<String> response = paymentService.createOrder();
		log.info("response from payment service : {} ", response);

		log.info("response status code : {}  ", response.getStatusCode());
		log.info("response status code value : {}  ", response.getStatusCodeValue());
		log.info("response headers : {}  ", response.getHeaders());
		log.info("response body : {}  ", response.getBody());

		return response;
	}

	@PostConstruct
	public void init() {
		log.info("Payment service interface called in controller : {}  ", paymentService);
	}
}
