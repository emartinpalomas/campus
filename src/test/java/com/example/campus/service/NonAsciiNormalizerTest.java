package com.example.campus.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonAsciiNormalizerTest {

    @ParameterizedTest
    @MethodSource("provideStringsForNormalization")
    public void testNormalize(String input, String expected) {
        NonAsciiNormalizer normalizer = new NonAsciiNormalizer();
        String actual = normalizer.normalize(input);
        assertEquals(expected, actual, "The normalize method did not return the expected result");
    }

    private static Stream<Arguments> provideStringsForNormalization() {
        return Stream.of(
                Arguments.of("Clàudia Martí", "claudiamarti"),
                Arguments.of("Noé Juárez", "noejuarez"),
                Arguments.of("Hèctor Cristòfol", "hectorcristofol"),
                Arguments.of("Asel·lia Purificació", "aselliapurificacio"),
                Arguments.of("Júlia Maçanet", "juliamacanet"),
                Arguments.of("Raül Cañas", "raulcanas"),
                Arguments.of("Lluïs Crête", "lluiscrete")
        );
    }
}
