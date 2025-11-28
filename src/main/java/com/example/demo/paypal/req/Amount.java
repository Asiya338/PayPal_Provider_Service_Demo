package com.example.demo.paypal.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Amount {

	@JsonProperty("currency_code")
	private String currencyCode;

	@JsonProperty("value")
	private String value;
}
