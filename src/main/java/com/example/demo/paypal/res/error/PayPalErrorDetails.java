package com.example.demo.paypal.res.error;

import lombok.Data;

@Data
public class PayPalErrorDetails {

	private String field;
	private String value;
	private String location;
	private String issue;
	private String description;
}
