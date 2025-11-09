package com.example.demo.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.service.TokenService;
import com.example.demo.service.interfaces.PaymentService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final TokenService tokenService;

	@Override
	public ResponseEntity<String> createOrder() {
		log.info("create order in payment service impl");

		ResponseEntity<String> response = tokenService.getAccessToken();
		log.info("access_token from token service : {} ", response);

		return response;
	}

	@PostConstruct
	public void init() {
		log.info("payment service impl initialized");
	}
}
