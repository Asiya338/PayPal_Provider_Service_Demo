package com.example.demo.paypal.res.create;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaypalOrder {

	private String id;
	private String status;

	@JsonProperty("payment_source")
	private PaymentSource paymentSource;

	private List<PaypalLinks> links;

}
