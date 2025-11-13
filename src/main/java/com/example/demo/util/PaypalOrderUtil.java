package com.example.demo.util;

import com.example.demo.paypal.res.error.capture.PaypalCaptureErrorRes;
import com.example.demo.paypal.res.error.create.PayPalErrorDetails;
import com.example.demo.paypal.res.error.create.PayPalErrorResponse;
import com.example.demo.paypal.res.error.show.PaypalShowErrorRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaypalOrderUtil {

	private PaypalOrderUtil() {
	}

	public static String generatePaypalErrorSummary(PayPalErrorResponse errorResponse) {
		log.error("Paypal Create Error Response : {} ", errorResponse);

		if (errorResponse == null) {
			return "No PayPal error details available. || CREATE ORDER FAILED";
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

	public static String generatePaypalCaptureErrorSummary(PaypalCaptureErrorRes errorResponse) {
		log.error("PayPal Capture Order Error Response : {}", errorResponse);

		if (errorResponse == null) {
			return "No PayPal error details available. || CAPTURE ORDER FAILED";
		}

		StringBuilder summary = new StringBuilder();

		// Append high-level fields
		if (errorResponse.getName() != null) {
			summary.append("Name: ").append(errorResponse.getName()).append(" | ");
		}

		if (errorResponse.getMessage() != null) {
			summary.append("Message: ").append(errorResponse.getMessage()).append(" | ");
		}

		// Extract first detail (if available)
		if (errorResponse.getDetails() != null && !errorResponse.getDetails().isEmpty()) {
			var detail = errorResponse.getDetails().get(0);

			if (detail.getIssue() != null) {
				summary.append("Issue: ").append(detail.getIssue()).append(" | ");
			}

			if (detail.getDescription() != null) {
				summary.append("Description: ").append(detail.getDescription()).append(" | ");
			}
		}

		// Trim trailing " | " if present
		int len = summary.length();
		if (len > 3 && summary.substring(len - 3).equals(" | ")) {
			summary.setLength(len - 3);
		}

		// If summary still empty, provide fallback
		if (summary.length() == 0) {
			summary.append("Unknown PayPal error occurred during CAPTURE ORDER");
		}

		log.info("Summary of PayPal Capture Error Response : {}", summary.toString());
		return summary.toString();
	}

	public static String generatePaypalShowErrorSummary(PaypalShowErrorRes errorResponse) {
		log.error("PayPal Capture Order Error Response : {}", errorResponse);

		if (errorResponse == null) {
			return "No PayPal error details available. || CAPTURE ORDER FAILED";
		}

		StringBuilder summary = new StringBuilder();

		// Append high-level fields
		if (errorResponse.getName() != null) {
			summary.append("Name: ").append(errorResponse.getName()).append(" | ");
		}

		if (errorResponse.getMessage() != null) {
			summary.append("Message: ").append(errorResponse.getMessage()).append(" | ");
		}

		// Extract first detail (if available)
		if (errorResponse.getDetails() != null && !errorResponse.getDetails().isEmpty()) {
			var detail = errorResponse.getDetails().get(0);

			if (detail.getIssue() != null) {
				summary.append("Issue: ").append(detail.getIssue()).append(" | ");
			}

			if (detail.getDescription() != null) {
				summary.append("Description: ").append(detail.getDescription()).append(" | ");
			}
		}

		// Trim trailing " | " if present
		int len = summary.length();
		if (len > 3 && summary.substring(len - 3).equals(" | ")) {
			summary.setLength(len - 3);
		}

		// If summary still empty, provide fallback
		if (summary.length() == 0) {
			summary.append("Unknown PayPal error occurred during CAPTURE ORDER");
		}

		log.info("Summary of PayPal Capture Error Response : {}", summary.toString());
		return summary.toString();
	}

}
