package com.example.campus.validation;

import com.example.campus.entity.DateRange;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class EndDateAfterStartDateValidatorTest {
    private EndDateAfterStartDateValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new EndDateAfterStartDateValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    public void testDateRangeIsNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    public void testStartDateIsNull() {
        DateRange dateRange = new DateRange(null, LocalDate.now().atStartOfDay());
        assertTrue(validator.isValid(dateRange, context));
    }

    @Test
    public void testEndDateIsNull() {
        DateRange dateRange = new DateRange(LocalDate.now().atStartOfDay(), null);
        assertTrue(validator.isValid(dateRange, context));
    }

    @Test
    public void testEndDateBeforeStartDate() {
        DateRange dateRange = new DateRange(LocalDate.now().atStartOfDay(), LocalDate.now().minusDays(1).atStartOfDay());
        assertFalse(validator.isValid(dateRange, context));
    }

    @Test
    public void testEndDateAfterStartDate() {
        DateRange dateRange = new DateRange(LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
        assertTrue(validator.isValid(dateRange, context));
    }

    @Test
    public void testEndDateEqualToStartDate() {
        DateRange dateRange = new DateRange(LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay());
        assertTrue(validator.isValid(dateRange, context));
    }
}
