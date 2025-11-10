package com.example.demo.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class PayPalProviderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public PayPalProviderException(String errorCode, String errorMessage, HttpStatus httpError) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.httpStatus = httpError;
	}
}
