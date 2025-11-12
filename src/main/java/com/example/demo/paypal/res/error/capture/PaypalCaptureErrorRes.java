package com.example.demo.paypal.res.error.capture;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PaypalCaptureErrorRes {
	private String name;
	private List<Details> details;
	private String message;

	@JsonProperty("debug_id")
	private String debugId;

	private List<Link> links;
}
