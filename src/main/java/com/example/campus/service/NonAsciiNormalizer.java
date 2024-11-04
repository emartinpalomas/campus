package com.example.campus.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;

@Service
public class NonAsciiNormalizer implements TextSanitizer {
    @Override
    public String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\s+", "")
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();
    }
}
