package com.example.demo.util;

import com.example.demo.paypal.res.error.PayPalErrorDetails;
import com.example.demo.paypal.res.error.PayPalErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaypalOrderUtil {

	private PaypalOrderUtil() {
	}

	public static String generatePaypalErrorSummary(PayPalErrorResponse errorResponse) {
		log.info("Paypal Error Response : {} ", errorResponse);

		if (errorResponse == null) {
			return "No PayPal error details available.";
		}

		StringBuilder summary = new StringBuilder();

		// Append main fields if present
		if (errorResponse.getName() != null) {
			summary.append("Name: ").append(errorResponse.getName()).append(" | ");
		}

		if (errorResponse.getMessage() != null) {
			summary.append("Message: ").append(errorResponse.getMessage()).append(" | ");
		}

		if (errorResponse.getError() != null) {
			summary.append("Error: ").append(errorResponse.getError()).append(" | ");
		}

		if (errorResponse.getErrorDescription() != null) {
			summary.append("Description: ").append(errorResponse.getErrorDescription()).append(" | ");
		}

		// Append first details entry (if present)
		if (errorResponse.getDetails() != null && !errorResponse.getDetails().isEmpty()) {
			PayPalErrorDetails detail = errorResponse.getDetails().get(0);

			if (detail.getField() != null) {
				summary.append("Field: ").append(detail.getField()).append(" | ");
			}

			if (detail.getIssue() != null) {
				summary.append("Issue: ").append(detail.getIssue()).append(" | ");
			}

			if (detail.getDescription() != null) {
				summary.append("Detail Description: ").append(detail.getDescription()).append(" | ");
			}
		}

		// Remove trailing " | " if present
		if (summary.length() > 3 && summary.substring(summary.length() - 3).equals(" | ")) {
			summary.setLength(summary.length() - 3);
		}

		log.info("Summary of paypal error response : {} ", summary.toString());
		return summary.toString();
	}

}
