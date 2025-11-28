package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.constant.ErrorCodeEnum;
import com.example.demo.exception.PayPalProviderException;
import com.example.demo.pojo.CreateOrderRes;

@ExtendWith(MockitoExtension.class)
public class CreateOrderResValidatorTest {

	@InjectMocks
	private CreateOrderResValidator createOrderResValidator;

	@Test
	void createOrderResValidatorTest_success() {
		CreateOrderRes createOrderRes = new CreateOrderRes("0RD_123", "PAYER_ACTION_REQUIRED",
				"https://www.paypal.com/checkoutnow?token=0RD123");

		assertDoesNotThrow(() -> createOrderResValidator.validateOrderRes(createOrderRes));
	}

	@Test
	void createOrderResValidatorTest_nullRes() {
		PayPalProviderException ex = assertThrows(PayPalProviderException.class, () -> {
			createOrderResValidator.validateOrderRes(null);
		});

		assertEquals(ErrorCodeEnum.CREATE_RESPONSE_ERROR.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.CREATE_RESPONSE_ERROR.getErrorMessage(), ex.getErrorMessage());
	}

	@Test
	void createOrderResValidatorTest_invalidOrderId() {
		CreateOrderRes createOrderRes = new CreateOrderRes(null, "PAYER_ACTION_REQUIRED",
				"https://www.paypal.com/checkoutnow?token=0RD123");

		PayPalProviderException ex = assertThrows(PayPalProviderException.class, () -> {
			createOrderResValidator.validateOrderRes(createOrderRes);
		});

		assertEquals(ErrorCodeEnum.CREATE_ID_ERROR.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.CREATE_ID_ERROR.getErrorMessage(), ex.getErrorMessage());
	}

	@Test
	void createOrderResValidatorTest_invalidPayStatus() {
		CreateOrderRes createOrderRes = new CreateOrderRes("0RD_123", null,
				"https://www.paypal.com/checkoutnow?token=0RD123");

		PayPalProviderException ex = assertThrows(PayPalProviderException.class, () -> {
			createOrderResValidator.validateOrderRes(createOrderRes);
		});

		assertEquals(ErrorCodeEnum.CREATE_STATUS_ERROR.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.CREATE_STATUS_ERROR.getErrorMessage(), ex.getErrorMessage());
	}

	@Test
	void createOrderResValidatorTest_invalidRedirectUrl() {
		CreateOrderRes createOrderRes = new CreateOrderRes("0RD_123", "PAYER_ACTION_REQUIRED", null);

		PayPalProviderException ex = assertThrows(PayPalProviderException.class, () -> {
			createOrderResValidator.validateOrderRes(createOrderRes);
		});

		assertEquals(ErrorCodeEnum.CREATE_URL_ERROR.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.CREATE_URL_ERROR.getErrorMessage(), ex.getErrorMessage());
	}

}
