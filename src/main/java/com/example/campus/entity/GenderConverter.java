package com.example.campus.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        return Optional.ofNullable(gender)
                .map(Gender::getDbValue)
                .orElse(null);
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbValue) {
        return Optional.ofNullable(dbValue)
                .map(Gender::fromDbValue)
                .orElse(Gender.NA);
    }
}
