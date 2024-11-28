package com.example.campus.validation;

import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SpainNationalIdValidator implements CountryNationalIdValidator {
    private static final Pattern SPAIN_ID_PATTERN = Pattern.compile("^[XYZ]?\\d{7,8}-[A-Z]$");
    private static final String SPAIN_ID_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final int LETTER_INDEX_DIVISOR = 23;

    @Override
    public boolean validate(String nationalId, ConstraintValidatorContext context) {
        if (!SPAIN_ID_PATTERN.matcher(nationalId).matches()) {
            return false;
        }
        return checkSpainId(nationalId);
    }

    private boolean checkSpainId(String nationalId) {
        try {
            int dniNumber = calculateDniNumber(nationalId);
            char expectedLetter = calculateExpectedLetter(dniNumber);
            char actualLetter = nationalId.charAt(nationalId.length() - 1);
            return expectedLetter == actualLetter;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int calculateDniNumber(String nationalId) {
        return switch (nationalId.charAt(0)) {
            case 'X' -> Integer.parseInt(nationalId.substring(1, 8));
            case 'Y' -> Integer.parseInt("1" + nationalId.substring(1, 8));
            case 'Z' -> Integer.parseInt("2" + nationalId.substring(1, 8));
            default -> Integer.parseInt(nationalId.substring(0, 8));
        };
    }

    private char calculateExpectedLetter(int dniNumber) {
        return SPAIN_ID_LETTERS.charAt(dniNumber % LETTER_INDEX_DIVISOR);
    }
}
