package com.hulkhiretech.payments.paypal.res.capture;

import lombok.Data;

@Data
public class Link {
	private String href;
	private String rel;
	private String method;
}
