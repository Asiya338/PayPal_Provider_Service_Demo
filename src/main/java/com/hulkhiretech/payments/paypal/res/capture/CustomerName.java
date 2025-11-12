package com.hulkhiretech.payments.paypal.res.capture;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CustomerName {

	@JsonProperty("given_name")
	private String givenName;

	private String surname;
}
