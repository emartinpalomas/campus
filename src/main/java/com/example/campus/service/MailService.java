package com.example.campus.service;

import com.example.campus.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public MailService(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    public void sendPasswordResetMail(User user, String token) {
        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setTo(user.getEmail());
        passwordResetEmail.setSubject(mailProperties.getResetPasswordSubject());
        String resetUrl = String.format(mailProperties.getResetPasswordUrl(), token);
        passwordResetEmail.setText(String.format(mailProperties.getResetPasswordText(), resetUrl));
        try {
            mailSender.send(passwordResetEmail);
            log.info("Password reset mail sent to {}", user.getEmail());
        } catch (MailException e) {
            log.error("Error occurred when sending password reset mail to {}", user.getEmail(), e);
        }
    }
}
