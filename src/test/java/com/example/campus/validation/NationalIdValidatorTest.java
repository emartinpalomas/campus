package com.example.campus.validation;

import com.example.campus.entity.User;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NationalIdValidatorTest {
    private NationalIdValidator validator;
    private ConstraintValidatorContext context;
    private User user;

    @BeforeEach
    public void setUp() {
        validator = new NationalIdValidator();
        context = mock(ConstraintValidatorContext.class);
        user = mock(User.class);
    }

    @Test
    public void testValidId() {
        when(user.getCountry()).thenReturn("chile");
        when(user.getNationalId()).thenReturn("12345678-5");
        assertTrue(validator.isValid(user, context));
    }

    @Test
    public void testInvalidId() {
        when(user.getCountry()).thenReturn("chile");
        when(user.getNationalId()).thenReturn("12345678-9");
        assertFalse(validator.isValid(user, context));
    }

    @Test
    public void testInvalidCountry() {
        when(user.getCountry()).thenReturn("invalidCountry");
        when(user.getNationalId()).thenReturn("12345678-9");
        assertTrue(validator.isValid(user, context));
    }

    @Test
    public void testEmptyId() {
        when(user.getCountry()).thenReturn("chile");
        when(user.getNationalId()).thenReturn("");
        assertFalse(validator.isValid(user, context));
    }
}
