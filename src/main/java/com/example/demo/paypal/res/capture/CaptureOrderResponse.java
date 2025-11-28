package com.example.demo.paypal.res.capture;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptureOrderResponse {

	private String id;
	private String status;

	@JsonProperty("payment_source")
	private PaymentSource paymentSource;

	@JsonProperty("purchase_units")
	private List<PurchaseUnits> pruchaseUnits;

	private Payer payer;
	private List<Link> links;

}
