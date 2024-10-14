package com.example.campus.service;

import com.example.campus.entity.PasswordResetToken;
import com.example.campus.entity.User;
import com.example.campus.exception.FailedToSendEmailException;
import com.example.campus.exception.InvalidTokenException;
import com.example.campus.exception.TokenExpiredException;
import com.example.campus.repository.PasswordResetTokenRepository;
import com.example.campus.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${token.expiry.duration}")
    private int tokenExpiryDuration;

    public UserService(
            UserRepository userRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            BCryptPasswordEncoder passwordEncoder,
            JavaMailSender mailSender,
            SpringTemplateEngine templateEngine
    ) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findByNationalIdAndCountry(user.getNationalId(), user.getCountry());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        String username = generateUsername(user);
        user.setUsername(username);
        initiatePasswordReset(user);
        log.info("User created with username: {}", username);
        return userRepository.save(user);
    }

    private String generateUsername(User user) {
        List<String> allUsernames = userRepository.findAllUsernames();
        String name = Normalizer.normalize(user.getName(), Normalizer.Form.NFD).replaceAll("\\s+", "").replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        String firstSurname = Normalizer.normalize(user.getFirstSurname(), Normalizer.Form.NFD).replaceAll("\\s+", "").replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        String secondSurname = user.getSecondSurname();
        if (secondSurname != null) {
            secondSurname = Normalizer.normalize(secondSurname, Normalizer.Form.NFD).replaceAll("\\s+", "").replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        }

        String baseUsername = name.charAt(0) + firstSurname;
        String username = baseUsername;
        if (allUsernames.contains(username) && secondSurname != null) {
            int index = 0;
            while (allUsernames.contains(username) && index < secondSurname.length()) {
                username = baseUsername + secondSurname.substring(0, ++index);
            }
        }
        int index = 1;
        while (allUsernames.contains(username) && index < name.length()) {
            username = name.substring(0, ++index) + firstSurname + secondSurname;
        }
        int num = 1;
        while (allUsernames.contains(username)) {
            username = baseUsername + num++;
        }
        return username;
    }

    public void initiatePasswordReset(User user) {
        String token = generateSecureToken();
        createPasswordResetTokenForUser(user, token);
    }

    private String generateSecureToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(token);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(tokenExpiryDuration));
        passwordResetTokenRepository.save(myToken);

        Context context = new Context();
        context.setVariable("resetUrl", "http://localhost:8080/reset?token=" + token);
        String emailContent = templateEngine.process("password-reset-email", context);

        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setFrom("");
        passwordResetEmail.setTo(user.getEmail());
        passwordResetEmail.setSubject("Password Reset Request");
        passwordResetEmail.setText(emailContent);

        final int MAX_ATTEMPTS = 3;
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            try {
                mailSender.send(passwordResetEmail);
                return;
            } catch (MailException e) {
                log.error("Attempt {} to send password reset email failed", i + 1, e);
                if (i < MAX_ATTEMPTS - 1) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        throw new FailedToSendEmailException("Failed to send password reset email after " + MAX_ATTEMPTS + " attempts");
    }

    public void validatePasswordResetToken(String token, String newPassword) {
        Optional<PasswordResetToken> maybeToken = passwordResetTokenRepository.findByToken(token);
        if (maybeToken.isPresent()) {
            PasswordResetToken resetToken = maybeToken.get();
            if (resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                User user = resetToken.getUser();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                passwordResetTokenRepository.delete(resetToken);
            } else {
                throw new TokenExpiredException("Token has expired");
            }
        } else {
            throw new InvalidTokenException("Invalid token");
        }
    }
}
