package com.example.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.constant.ErrorCodeEnum;
import com.example.demo.exception.PayPalProviderException;
import com.example.demo.pojo.CreateOrderReq;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentValidator {

	public void validateService(CreateOrderReq createOrderReq) {
		if (createOrderReq.getCurrencyCode() == null || createOrderReq.getCurrencyCode().isBlank()) {
			log.error("Currency code is required field and can't be null/blank");
			throw new PayPalProviderException(ErrorCodeEnum.CURRENCY_CODE_REQUIRED.getErrorCode(),
					ErrorCodeEnum.CURRENCY_CODE_REQUIRED.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}

		if (createOrderReq.getAmount() <= 0) {
			log.error("Amount is required field and can't be negative");
			throw new PayPalProviderException(ErrorCodeEnum.INVALID_AMOUNT.getErrorCode(),
					ErrorCodeEnum.INVALID_AMOUNT.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}

		if (createOrderReq.getReturnUrl() == null || createOrderReq.getReturnUrl().isBlank()) {
			log.error("ReturnUrl is required field and can't be null/blank");
			throw new PayPalProviderException(ErrorCodeEnum.RETURN_URL_REQUIRED.getErrorCode(),
					ErrorCodeEnum.RETURN_URL_REQUIRED.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}

		if (createOrderReq.getCancelUrl() == null || createOrderReq.getCancelUrl().isBlank()) {
			log.error("CancelUrl is required field and can't be null/blank");
			throw new PayPalProviderException(ErrorCodeEnum.CANCEL_URL_REQUIRED.getErrorCode(),
					ErrorCodeEnum.CANCEL_URL_REQUIRED.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}

	}

}
