package com.ridebooking.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CardDetailsRequiredValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CardDetailsRequired {
    String message() default "Card details are required when payment method is CARD";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
