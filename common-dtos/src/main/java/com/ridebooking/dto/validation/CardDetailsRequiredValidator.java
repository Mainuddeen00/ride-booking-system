package com.ridebooking.dto.validation;

import com.ridebooking.dto.PaymentRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CardDetailsRequiredValidator implements ConstraintValidator<CardDetailsRequired, PaymentRequestDTO> {

    @Override
    public boolean isValid(PaymentRequestDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if ("CARD".equalsIgnoreCase(value.getPaymentMethod())) {
            return value.getCardDetails() != null;
        }
        return true;
    }
}
