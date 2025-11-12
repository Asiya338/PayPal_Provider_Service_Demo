package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

	GENERIC_ERROR("30000", "Something went wrong. Please try later"),
	CURRENCY_CODE_REQUIRED("30001", "Currency code is a required field and cannot be null or blank"),
	RETURN_URL_REQUIRED("30002", "return url required"),
	INVALID_AMOUNT("30004", "Amount is required field and must be positive"),
	CANCEL_URL_REQUIRED("30003", "Cancel Url required"), PAYPAL_ERROR("30004", "<Error in paypal system>"),
	PAYPAL_SERVICE_UNAVAILABLE("30005", "Unable to connect to PayPal. Please try again later."),
	TO_JSON_ERROR("30006", "ERROR CONVERTING TO JSON"), FROM_JSON_ERROR("30007", "ERROR CONVERTING FROM JSON TO"),
	PAYPAL_UNKNOWN_ERROR("30008", "Unknown error occured while processing paypal request... "),
	CREATE_RESPONSE_ERROR("30009", "Create Order request has to be created and response must not be null"),
	CREATE_ID_ERROR("30010", "Order ID must not be null"),
	CREATE_STATUS_ERROR("30011", "Order status must be valid and cannot be null"),
	CREATE_URL_ERROR("30012", "Order response must have redirection url to capture order"),
	RESOURCE_NOT_FOUND("30013", "Resource not found, Please try with correct request");

	private final String errorCode;
	private final String errorMessage;

	ErrorCodeEnum(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
