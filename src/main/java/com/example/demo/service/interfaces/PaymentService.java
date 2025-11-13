package com.example.demo.service.interfaces;

import org.springframework.stereotype.Service;

import com.example.demo.pojo.CaptureOrderRes;
import com.example.demo.pojo.CreateOrderReq;
import com.example.demo.pojo.CreateOrderRes;
import com.example.demo.pojo.ShowOrderRes;

@Service
public interface PaymentService {

	public CreateOrderRes createOrder(CreateOrderReq createOrderReq);

	public CaptureOrderRes captureOrder(String orderId);

	public ShowOrderRes showOrder(String orderId);
}
