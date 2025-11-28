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
import com.example.demo.pojo.CreateOrderReq;

@ExtendWith(MockitoExtension.class)
public class PaymentValidatorTest {

	@InjectMocks
	private PaymentValidator paymentValidator;

	@Test
	void validateServiceTest_success() {
		CreateOrderReq createOrderReq = new CreateOrderReq("USD", 100.0, "http://return.url", "http://cancel.url");

		assertDoesNotThrow(() -> paymentValidator.validateService(createOrderReq));
	}

	@Test
	void validateServiceTest_NullReq() {
		PayPalProviderException ex = assertThrows(PayPalProviderException.class, () -> {
			paymentValidator.validateService(null);
		});

		assertEquals(ErrorCodeEnum.CREATE_ORDER_REQ_NULL.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.CREATE_ORDER_REQ_NULL.getErrorMessage(), ex.getErrorMessage());
	}

	@Test
	void validateServiceTest_InvalidCurrencyCode() {
		CreateOrderReq createOrderReq = new CreateOrderReq("", 100.0, "http://return.url", "http://cancel.url");

		PayPalProviderException ex = assertThrows(PayPalProviderException.class, () -> {
			paymentValidator.validateService(createOrderReq);
		});

		assertEquals(ErrorCodeEnum.CURRENCY_CODE_REQUIRED.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.CURRENCY_CODE_REQUIRED.getErrorMessage(), ex.getErrorMessage());
	}

	@Test
	void validateServiceTest_InvalidAmount() {
		CreateOrderReq createOrderReq = new CreateOrderReq("USD", -100.0, "http://return.url", "http://cancel.url");

		PayPalProviderException ex = assertThrows(PayPalProviderException.class, () -> {
			paymentValidator.validateService(createOrderReq);
		});

		assertEquals(ErrorCodeEnum.INVALID_AMOUNT.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.INVALID_AMOUNT.getErrorMessage(), ex.getErrorMessage());
	}

	@Test
	void validateServiceTest_returnUrlReq() {
		CreateOrderReq createOrderReq = new CreateOrderReq("USD", 100.0, null, "http://cancel.url");

		PayPalProviderException ex = assertThrows(PayPalProviderException.class, () -> {
			paymentValidator.validateService(createOrderReq);
		});

		assertEquals(ErrorCodeEnum.RETURN_URL_REQUIRED.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.RETURN_URL_REQUIRED.getErrorMessage(), ex.getErrorMessage());
	}

	@Test
	void validateServiceTest_cancelUrlReq() {
		CreateOrderReq createOrderReq = new CreateOrderReq("USD", 100.0, "http://return.url", null);

		PayPalProviderException ex = assertThrows(PayPalProviderException.class, () -> {
			paymentValidator.validateService(createOrderReq);
		});

		assertEquals(ErrorCodeEnum.CANCEL_URL_REQUIRED.getErrorCode(), ex.getErrorCode());
		assertEquals(ErrorCodeEnum.CANCEL_URL_REQUIRED.getErrorMessage(), ex.getErrorMessage());
	}

}
