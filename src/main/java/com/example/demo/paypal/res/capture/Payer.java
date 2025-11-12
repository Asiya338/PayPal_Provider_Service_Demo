package com.example.demo.paypal.res.capture;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Payer {
	private CustomerName name;

	@JsonProperty("email_address")
	private String emailAddress;

	@JsonProperty("payer_id")
	private String payerId;
}
