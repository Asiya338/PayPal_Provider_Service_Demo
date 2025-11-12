package com.example.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.constant.ErrorCodeEnum;
import com.example.demo.exception.PayPalProviderException;
import com.example.demo.pojo.CreateOrderRes;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreateOrderResValidator {

	public void validateOrderRes(CreateOrderRes createOrderRes) {
		if (createOrderRes == null) {
			log.error("create order res must not be null : {} ", createOrderRes);

			throw new PayPalProviderException(ErrorCodeEnum.CREATE_RESPONSE_ERROR.getErrorCode(),
					ErrorCodeEnum.CREATE_RESPONSE_ERROR.getErrorMessage(), HttpStatus.NOT_FOUND);
		}

		if (createOrderRes.getOrderId() == null) {
			log.error("Create order response id is null");

			throw new PayPalProviderException(ErrorCodeEnum.CREATE_ID_ERROR.getErrorCode(),
					ErrorCodeEnum.CREATE_ID_ERROR.getErrorMessage(), HttpStatus.NOT_FOUND);
		}

		if (createOrderRes.getPaymentStatus() == null) {
			log.error("Create order response status is null");

			throw new PayPalProviderException(ErrorCodeEnum.CREATE_STATUS_ERROR.getErrorCode(),
					ErrorCodeEnum.CREATE_STATUS_ERROR.getErrorMessage(), HttpStatus.NOT_FOUND);
		}

		if (createOrderRes.getRedirectUrl() == null) {
			log.error("Create order redirect Url is null");

			throw new PayPalProviderException(ErrorCodeEnum.CREATE_URL_ERROR.getErrorCode(),
					ErrorCodeEnum.CREATE_URL_ERROR.getErrorMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
