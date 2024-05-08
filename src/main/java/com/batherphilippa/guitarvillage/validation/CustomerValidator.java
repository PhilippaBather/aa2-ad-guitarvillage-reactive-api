package com.batherphilippa.guitarvillage.validation;

import com.batherphilippa.guitarvillage.domain.Customer;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CustomerValidator implements Validator {

    private static final String FIELD_REQUIRED = "field.required";

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "forename", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", FIELD_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tel", FIELD_REQUIRED);
    }
}
