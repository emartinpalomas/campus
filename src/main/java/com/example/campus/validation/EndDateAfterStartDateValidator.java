package com.example.campus.validation;

import com.example.campus.entity.DateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, DateRange> {
    @Override
    public boolean isValid(DateRange dateRange, ConstraintValidatorContext context) {
        if (dateRange == null || dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
            return true;
        }
        return !dateRange.getEndDate().isBefore(dateRange.getStartDate());
    }
}
