package com.example.campus.entity;

import java.util.Optional;

public enum Gender {
    MALE("male", 1),
    FEMALE("female", 2),
    NA("NA", null);

    private final String label;
    private final Integer dbValue;

    Gender(String label, Integer dbValue) {
        this.label = label;
        this.dbValue = dbValue;
    }

    public String getLabel() {
        return label;
    }

    public Integer getDbValue() {
        return dbValue;
    }

    public static Gender fromDbValue(Integer dbValue) {
        return Optional.ofNullable(dbValue)
                .flatMap(dbv -> {
                    for (Gender gender : Gender.values()) {
                        if ((gender.dbValue == null && dbv == null) || (gender.dbValue != null && gender.dbValue.equals(dbv))) {
                            return Optional.of(gender);
                        }
                    }
                    return Optional.empty();
                })
                .orElse(Gender.NA);
    }
}
