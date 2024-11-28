package com.example.campus.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class SpainNationalIdValidatorTest {
    private final SpainNationalIdValidator validator = new SpainNationalIdValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    public void testNIEStartingWithX() {
        String nationalId = "X1234567-L";
        assertTrue(validator.validate(nationalId, context));
    }

    @Test
    public void testNIEStartingWithY() {
        String nationalId = "Y1234567-X";
        assertTrue(validator.validate(nationalId, context));
    }

    @Test
    public void testNIEStartingWithZ() {
        String nationalId = "Z1234567-R";
        assertTrue(validator.validate(nationalId, context));
    }

    @Test
    public void testDNI() {
        String nationalId = "12345678-Z";
        assertTrue(validator.validate(nationalId, context));
    }

    @Test
    public void testInvalidLetter() {
        String nationalId = "X1234567-A";
        assertFalse(validator.validate(nationalId, context));
    }

    @Test
    public void testInvalidNumberFormat() {
        String nationalId = "X12A4567-L";
        assertFalse(validator.validate(nationalId, context));
    }

    @Test
    public void testInvalidNumberOfDigits() {
        String nationalId = "X123456789-A";
        assertFalse(validator.validate(nationalId, context));
    }

    @Test
    public void testEmptyId() {
        String nationalId = "";
        assertFalse(validator.validate(nationalId, context));
    }
}
