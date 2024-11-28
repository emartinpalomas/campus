package com.example.campus.validation;

import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ChileNationalIdValidator implements CountryNationalIdValidator {
    private static final Pattern CHILE_ID_PATTERN = Pattern.compile("^\\d{7,8}-[\\dK]$");
    private static final int[] SERIES = {2, 3, 4, 5, 6, 7};
    private static final int REMAINDER_DIVISOR = 11;

    @Override
    public boolean validate(String nationalId, ConstraintValidatorContext context) {
        if (!CHILE_ID_PATTERN.matcher(nationalId).matches()) {
            return false;
        }
        return checkChileId(nationalId);
    }

    private boolean checkChileId(String nationalId) {
        try {
            String[] parts = nationalId.split("-");
            int sum = calculateSum(parts[0]);
            int remainder = calculateRemainder(sum);
            return checkRemainder(remainder, parts[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private int calculateSum(String digits) {
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            sum += Character.getNumericValue(digits.charAt(digits.length() - 1 - i)) * SERIES[i % SERIES.length];
        }
        return sum;
    }

    private int calculateRemainder(int sum) {
        return REMAINDER_DIVISOR - (sum % REMAINDER_DIVISOR);
    }

    private boolean checkRemainder(int remainder, String checkDigit) {
        if (remainder == 10 && checkDigit.equalsIgnoreCase("K")) {
            return true;
        } else {
            return remainder == Integer.parseInt(checkDigit);
        }
    }
}
