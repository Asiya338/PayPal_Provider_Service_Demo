package com.example.demo.paypal.res.show;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShowOrderResponse {
	private String id;
	private String intent;
	private String status;

	@JsonProperty("payment_source")
	private PaymentSource paymentSource;

	@JsonProperty("purchase_units")
	private List<PurchaseUnit> purchaseUnits;

	@JsonProperty("create_time")
	private String createTime;

	private List<Link> links;

}
