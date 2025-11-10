package com.example.demo.pojo;

import lombok.Data;

@Data
public class CreateOrderRes {
	private String orderId;
	private String paymentStatus;
	private String redirectUrl;
}
