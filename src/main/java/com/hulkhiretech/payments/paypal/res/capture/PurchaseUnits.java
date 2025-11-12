package com.hulkhiretech.payments.paypal.res.capture;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PurchaseUnits {
	@JsonProperty("reference_id")
	private String referenceId;
	private Shipping shipping;
	private Payments payments;
}
