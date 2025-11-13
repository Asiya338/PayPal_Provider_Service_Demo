package com.example.demo.paypal.res.show;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Payee {

	@JsonProperty("email_address")
	private String emailAddress;

	@JsonProperty("merchant_id")
	private String merchantId;
}