package com.example.demo.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

	public ResponseEntity<String> createOrder();
}
