package com.example.campus.service;

public class DummyNormalizer implements TextSanitizer{
    @Override
    public String normalize(String input) {
        return input;
    }
}
