package com.batherphilippa.guitarvillage.validation;

import com.batherphilippa.guitarvillage.domain.Guitar;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

public class GuitarValidator implements Validator {

    private static final String FIELD_REQUIRED = "field.required";
    private static final String NEGATIVE_VALUE = "negative.value";
    @Override
    public boolean supports(Class<?> clazz) {
        return Guitar.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "make", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "model", FIELD_REQUIRED);

        Guitar g = (Guitar) target;
        if (g.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("price", NEGATIVE_VALUE);
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "instrumentType", FIELD_REQUIRED);
    }
}
