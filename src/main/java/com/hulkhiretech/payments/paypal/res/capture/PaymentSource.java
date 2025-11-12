package com.hulkhiretech.payments.paypal.res.capture;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PaymentSource {

	private Paypal paypal;

	@JsonProperty("email_address")
	private String emailAddress;

	@JsonProperty("account_id")
	private String accountId;

}
