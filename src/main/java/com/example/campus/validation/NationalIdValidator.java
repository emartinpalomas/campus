package com.example.campus.validation;

import com.example.campus.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NationalIdValidator implements ConstraintValidator<ValidNationalId, User> {
    private static final Pattern CHILE_ID_PATTERN = Pattern.compile("^\\d{7,8}-[\\dK]$");
    private static final Pattern SPAIN_ID_PATTERN = Pattern.compile("^[XYZ]?\\d{7,8}-[A-Z]$");
    private static final String SPAIN_ID_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        String nationalId = user.getNationalId();
        String country = user.getCountry().toLowerCase();

        switch (country) {
            case "chile":
                if (!CHILE_ID_PATTERN.matcher(nationalId).matches()) {
                    return false;
                }
                return checkChileId(nationalId);
            case "spain":
                if (!SPAIN_ID_PATTERN.matcher(nationalId).matches()) {
                    return false;
                }
                return checkSpainId(nationalId);
            default:
                return nationalId.matches("^[a-zA-Z0-9]*$");
        }
    }

    private boolean checkChileId(String nationalId) {
        int[] series = {2, 3, 4, 5, 6, 7};
        int sum = 0;
        String digits = nationalId.split("-")[0];
        for (int i = 0; i < digits.length(); i++) {
            sum += Character.getNumericValue(digits.charAt(digits.length() - 1 - i)) * series[i % series.length];
        }
        int remainder = sum % 11;
        String checkDigit = nationalId.split("-")[1];
        if (remainder == 10 && checkDigit.equalsIgnoreCase("K")) {
            return true;
        } else {
            return remainder == Integer.parseInt(checkDigit);
        }
    }

    private boolean checkSpainId(String nationalId) {
        int dniNumber;
        if (nationalId.startsWith("X")) {
            dniNumber = Integer.parseInt(nationalId.substring(1, 8));
        } else if (nationalId.startsWith("Y")) {
            dniNumber = Integer.parseInt("1" + nationalId.substring(1, 8));
        } else if (nationalId.startsWith("Z")) {
            dniNumber = Integer.parseInt("2" + nationalId.substring(1, 8));
        } else {
            dniNumber = Integer.parseInt(nationalId.substring(0, 8));
        }

        char expectedLetter = SPAIN_ID_LETTERS.charAt(dniNumber % 23);
        char actualLetter = nationalId.charAt(nationalId.length() - 1);

        return expectedLetter == actualLetter;
    }
}
