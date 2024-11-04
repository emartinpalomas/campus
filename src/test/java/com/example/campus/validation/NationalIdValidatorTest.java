package com.example.campus.validation;

import com.example.campus.entity.NationalIdInfo;
import com.example.campus.entity.User;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        NationalIdInfo nationalIdInfo = mock(NationalIdInfo.class);
        when(user.getNationalIdInfo()).thenReturn(nationalIdInfo);
    }

    @Test
    public void testValidId() {
        when(user.getNationalIdInfo().getCountry()).thenReturn("chile");
        when(user.getNationalIdInfo().getNationalId()).thenReturn("12345678-5");
        assertTrue(validator.isValid(user.getNationalIdInfo(), context));
    }

    @Test
    public void testNullNationalId() {
        when(user.getNationalIdInfo().getCountry()).thenReturn("chile");
        when(user.getNationalIdInfo().getNationalId()).thenReturn(null);
        assertFalse(validator.isValid(user.getNationalIdInfo(), context));
    }

    @Test
    public void testNullCountry() {
        when(user.getNationalIdInfo().getCountry()).thenReturn(null);
        when(user.getNationalIdInfo().getNationalId()).thenReturn("12345678-5");
        assertFalse(validator.isValid(user.getNationalIdInfo(), context));
    }

    @Test
    public void testNullNationalIdAndCountry() {
        when(user.getNationalIdInfo().getCountry()).thenReturn(null);
        when(user.getNationalIdInfo().getNationalId()).thenReturn(null);
        assertFalse(validator.isValid(user.getNationalIdInfo(), context));
    }

    @Test
    public void testInvalidId() {
        when(user.getNationalIdInfo().getCountry()).thenReturn("chile");
        when(user.getNationalIdInfo().getNationalId()).thenReturn("12345678-9");
        assertFalse(validator.isValid(user.getNationalIdInfo(), context));
    }

    @Test
    public void testInvalidCountry() {
        when(user.getNationalIdInfo().getCountry()).thenReturn("invalidCountry");
        when(user.getNationalIdInfo().getNationalId()).thenReturn("12345678-9");
        assertTrue(validator.isValid(user.getNationalIdInfo(), context));
    }

    @Test
    public void testEmptyId() {
        when(user.getNationalIdInfo().getCountry()).thenReturn("chile");
        when(user.getNationalIdInfo().getNationalId()).thenReturn("");
        assertFalse(validator.isValid(user.getNationalIdInfo(), context));
    }
}
