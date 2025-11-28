package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderReq {
	private String currencyCode;
	private double amount;
	private String returnUrl;
	private String cancelUrl;
}
