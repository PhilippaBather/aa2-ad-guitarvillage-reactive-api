package com.batherphilippa.guitarvillage.validation;

import com.batherphilippa.guitarvillage.domain.Order;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

public class OrderValidator implements Validator {

    private static final String FIELD_REQUIRED = "field.required";

    private static final String NEGATIVE_VALUE = "negative.value";

    @Override
    public boolean supports(Class<?> clazz) {
        return Order.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creationDate", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customerId", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "productId", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", FIELD_REQUIRED);

        Order o = (Order) target;
        if (o.getQuantity() <= 0) {
            errors.rejectValue("quantity", NEGATIVE_VALUE);
        }

        if (o.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("price", NEGATIVE_VALUE);
        }

    }
}
