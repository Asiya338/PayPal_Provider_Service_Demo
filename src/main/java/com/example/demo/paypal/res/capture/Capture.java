package com.example.demo.paypal.res.capture;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Capture {
	private String id;
	private String status;
	private Amount amount;

	@JsonProperty("seller_protection")
	private SellerProtection sellerProtection;

	@JsonProperty("final_capture")
	private boolean finalCapture;

	@JsonProperty("disbursement_mode")
	private String disbursementMode;

	@JsonProperty("seller_receivable_breakdown")
	private SellerReceivableBreakdown sellerReceivableBreakdown;

	@JsonProperty("create_time")
	private String createTime;

	@JsonProperty("update_time")
	private String updateTime;

	private List<Link> links;

}
