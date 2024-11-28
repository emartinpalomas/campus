package com.example.campus.validation;

import com.example.campus.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashMap;
import java.util.Map;

public class NationalIdValidator implements ConstraintValidator<ValidNationalId, User> {
    private final Map<String, CountryNationalIdValidator> validators = new HashMap<>();

    public NationalIdValidator() {
        validators.put("chile", new ChileNationalIdValidator());
        validators.put("spain", new SpainNationalIdValidator());
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        String country = user.getCountry().toLowerCase();
        String nationalId = user.getNationalId();
        CountryNationalIdValidator validator = validators.get(country);
        if (validator == null) {
            return nationalId.matches("^[a-zA-Z0-9-]*$");
        }
        return validator.validate(nationalId, context);
    }
}
