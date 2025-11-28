package com.example.demo.paypal.res.error.show;

import java.util.List;

import com.example.demo.paypal.res.error.capture.Details;
import com.example.demo.paypal.res.show.Link;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalShowErrorRes {
	private String name;
	private List<Details> details;
	private String message;

	@JsonProperty("debug_id")
	private String debugId;

	private List<Link> links;
}
