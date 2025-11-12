package com.hulkhiretech.payments.paypal.res.capture;

import java.util.List;

import lombok.Data;

@Data
public class Payments {
	private List<Capture> captures;
}
