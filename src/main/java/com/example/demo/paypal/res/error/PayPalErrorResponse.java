package com.example.demo.paypal.res.error;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PayPalErrorResponse {

	private String error;

	@JsonProperty("error_description")
	private String errorDescription;

	private String name;
	private String message;

	@JsonProperty("debug_id")
	private String debugId;

	private List<PayPalErrorDetails> details;

	private List<PayPalErrorLinks> links;

}
