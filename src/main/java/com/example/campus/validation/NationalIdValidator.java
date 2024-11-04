package com.example.campus.validation;

import com.example.campus.entity.NationalIdInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NationalIdValidator implements ConstraintValidator<ValidNationalId, NationalIdInfo> {
    private final Map<String, CountryNationalIdValidator> validators = new HashMap<>();

    public NationalIdValidator() {
        validators.put("chile", new ChileNationalIdValidator());
        validators.put("spain", new SpainNationalIdValidator());
    }

    @Override
    public boolean isValid(NationalIdInfo nationalIdInfo, ConstraintValidatorContext context) {
        String nationalId = nationalIdInfo.getNationalId();
        String country = nationalIdInfo.getCountry();
        log.info("Validating national ID: {}", nationalId);
        if (nationalId == null || country == null) {
            log.info("National ID or country is null");
            return false;
        }
        CountryNationalIdValidator validator = validators.get(country.toLowerCase());
        if (validator == null) {
            return nationalId.matches("^[a-zA-Z0-9-]*$");
        }
        return validator.validate(nationalId, context);
    }
}
