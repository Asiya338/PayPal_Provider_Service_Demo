package com.example.demo.paypal.res.capture;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SellerReceivableBreakdown {
	@JsonProperty("gross_amount")
	private Amount grossAmount;

	@JsonProperty("paypal_fee")
	private Amount paypalFee;

	@JsonProperty("net_amount")
	private Amount netAmount;
}
