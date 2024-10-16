package com.example.campus.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ChileNationalIdValidatorTest {
    private final ChileNationalIdValidator validator = new ChileNationalIdValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    public void testValidId() {
        String nationalId = "12345678-5";
        assertTrue(validator.validate(nationalId, context));
    }

    @Test
    public void testInvalidId() {
        String nationalId = "12345678-9";
        assertFalse(validator.validate(nationalId, context));
    }

    @Test
    public void testInvalidNumberOfDigits() {
        String nationalId = "123456789-5";
        assertFalse(validator.validate(nationalId, context));
    }

    @Test
    public void testEmptyId() {
        String nationalId = "";
        assertFalse(validator.validate(nationalId, context));
    }
}