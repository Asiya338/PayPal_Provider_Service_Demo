package com.example.demo.paypal.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {

	private String intent;

	@JsonProperty("purchase_units")
	private List<PurchaseUnits> purchaseUnits;

	@JsonProperty("payment_source")
	private PaymentSource paymentSource;
}
