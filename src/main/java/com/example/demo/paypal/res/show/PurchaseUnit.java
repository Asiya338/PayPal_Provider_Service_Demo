package com.example.demo.paypal.res.show;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PurchaseUnit {

	@JsonProperty("reference_id")
	private String referenceId;

	private Amount amount;
	private Payee payee;
}