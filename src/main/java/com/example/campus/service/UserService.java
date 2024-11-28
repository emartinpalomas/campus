package com.example.campus.service;

import com.example.campus.entity.User;
import com.example.campus.exception.UserAlreadyExistsException;
import com.example.campus.exception.UserCreationFailedException;
import com.example.campus.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    public static final int USERNAME_MAX_LENGTH = 20;
    public static final int MAX_RETRIES = 3;
    private final UserRepository userRepository;
    private final TextSanitizer textSanitizer;

    public UserService(
            UserRepository userRepository,
            TextSanitizer textSanitizer
    ) {
        this.userRepository = userRepository;
        this.textSanitizer = textSanitizer;
    }

    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findByNationalIdAndCountry(user.getNationalId(), user.getCountry());
        if (existingUser.isPresent()) {
            log.error("Attempted to create user that already exists: {}", user);
            throw new UserAlreadyExistsException("User already exists");
        }
        String username = generateUsername(user);
        user.setUsername(username);
        log.info("User created with username: {}", username);

        int retryCount = 0;
        int maxRetries = MAX_RETRIES;

        Throwable cause = null;
        while (retryCount < maxRetries) {
            try {
                return userRepository.save(user);
            } catch (DataIntegrityViolationException e) {
                cause = e.getCause();
                log.error("Data integrity violation: {}", cause != null ? cause.getMessage() : "Unknown cause");
                user.setUsername(generateUsername(user));
                retryCount++;
            }
        }
        throw new UserCreationFailedException("Failed to create user after " + maxRetries + " attempts", cause);
    }

    private String generateUsername(User user) {
        Set<String> allUsernames = new HashSet<>(userRepository.findAllUsernames());
        String name = textSanitizer.normalize(user.getName());
        String firstSurname = textSanitizer.normalize(user.getFirstSurname());
        String secondSurname = user.getSecondSurname();
        if (secondSurname != null) secondSurname = textSanitizer.normalize(secondSurname);

        String potentialUsername = obtainPotentialUsername(allUsernames, name, firstSurname, secondSurname, "");
        if (potentialUsername != null) return potentialUsername;

        int num = 1;
        while (true) {
            potentialUsername = obtainPotentialUsername(allUsernames, name, firstSurname, secondSurname, String.valueOf(num));
            if (potentialUsername != null) return potentialUsername;
            num++;
        }
    }

    private static String obtainPotentialUsername(Set<String> allUsernames, String name, String firstSurname, String secondSurname, String num) {
        for (int nameIndex = 1; nameIndex <= name.length(); nameIndex++) {
            if (secondSurname != null) {
                for (int secondSurnameIndex = 0; secondSurnameIndex <= secondSurname.length(); secondSurnameIndex++) {
                    StringBuilder potentialUsername = new StringBuilder(name.substring(0, nameIndex) + firstSurname + secondSurname.substring(0, secondSurnameIndex));
                    StringBuilder username = evaluatePotentialUsername(allUsernames, num, potentialUsername);
                    if (username != null) return username.toString();
                }
            } else {
                StringBuilder potentialUsername = new StringBuilder(name.substring(0, nameIndex) + firstSurname);
                StringBuilder username = evaluatePotentialUsername(allUsernames, num, potentialUsername);
                if (username != null) return username.toString();
            }
        }
        return null;
    }

    private static StringBuilder evaluatePotentialUsername(Set<String> allUsernames, String num, StringBuilder potentialUsername) {
        if (potentialUsername.length() > USERNAME_MAX_LENGTH) {
            potentialUsername = new StringBuilder(potentialUsername.substring(0, USERNAME_MAX_LENGTH));
        }
        if (!Objects.equals(num, "")) {
            potentialUsername.append(num);
        }
        if (!allUsernames.contains(potentialUsername.toString())) {
            return potentialUsername;
        }
        return null;
    }
}
