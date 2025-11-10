package com.example.demo.service.interfaces;

import org.springframework.stereotype.Service;

import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.pojo.CreateOrderRes;

@Service
public interface PaymentService {

	public CreateOrderRes createOrder(CreateOrderReq createOrderReq);
}
