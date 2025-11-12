package com.example.demo.paypal.res.capture;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SellerProtection {
	private String status;
	@JsonProperty("dispute_categories")
	private List<String> disputeCategories;
}
