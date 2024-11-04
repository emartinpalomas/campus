package com.example.campus.service;

import com.example.campus.entity.PasswordResetToken;
import com.example.campus.entity.User;
import com.example.campus.exception.InvalidTokenException;
import com.example.campus.exception.UserNotFoundException;
import com.example.campus.repository.PasswordResetTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;

    public PasswordResetService(UserService userService,
                                PasswordResetTokenRepository passwordResetTokenRepository,
                                BCryptPasswordEncoder passwordEncoder,
                                MailService mailService
    ) {
        this.userService = userService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public void createPasswordResetTokenForUser(String username) throws UserNotFoundException {
        User user = userService.findUserByUsername(username);
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(passwordResetToken);

        mailService.sendPasswordResetMail(user, token);
    }

    public void resetPassword(String token, String newPassword) throws InvalidTokenException {
        if (validatePasswordResetToken(token)) {
            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
            User user = passwordResetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);
            passwordResetTokenRepository.delete(passwordResetToken);
        } else {
            throw new InvalidTokenException("Invalid or expired token");
        }
    }

    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        return passwordResetToken != null && passwordResetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }
}
