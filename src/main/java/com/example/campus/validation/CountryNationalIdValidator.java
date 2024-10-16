package com.example.campus.validation;

import jakarta.validation.ConstraintValidatorContext;

public interface CountryNationalIdValidator {
    boolean validate(String nationalId, ConstraintValidatorContext context);
}
