package com.example.demo.pojo;

import lombok.Data;

@Data
public class CreateOrderReq {
	private String currencyCode;
	private double amount;
	private String returnUrl;
	private String cancelUrl;
}
