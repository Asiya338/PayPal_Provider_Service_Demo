package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.constant.ErrorCodeEnum;
import com.example.demo.pojo.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(PayPalProviderException.class)
	public ResponseEntity<ErrorResponse> handlePayPalProviderException(PayPalProviderException ex) {
		log.error("Handling PayPalProviderException: {}", ex.getErrorMessage(), ex);

		ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());

		return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		log.error("Handling Exception : {} ", ex.getMessage(), ex);

		ErrorResponse errorResponse = new ErrorResponse(ErrorCodeEnum.GENERIC_ERROR.getErrorCode(),
				ErrorCodeEnum.GENERIC_ERROR.getErrorMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> resourceNotFoundEception(ResourceNotFoundException ex) {
		log.error("Handling ResourceNotFound Exception: {}", ex.getErrorMessage(), ex);

		ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());

		return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
	}

}
