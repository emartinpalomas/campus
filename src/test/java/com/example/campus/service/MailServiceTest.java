package com.example.campus.service;

import com.example.campus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MailServiceTest {

    private MailService mailService;
    private JavaMailSender mailSender;

    @BeforeEach
    public void setup() {
        mailSender = Mockito.mock(JavaMailSender.class);
        MailProperties mailProperties = Mockito.mock(MailProperties.class);

        when(mailProperties.getResetPasswordUrl()).thenReturn("http://localhost:8080/reset-password?token=%s");
        when(mailProperties.getResetPasswordSubject()).thenReturn("Password Reset Request");
        when(mailProperties.getResetPasswordText()).thenReturn("To reset your password, click the link below:\n%s");

        mailService = new MailService(mailSender, mailProperties);
    }

    @Test
    public void testSendPasswordResetMail() {
        User user = new User();
        user.setEmail("test@example.com");
        String token = "testToken";

        mailService.sendPasswordResetMail(user, token);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage messageSent = captor.getValue();
        if (messageSent.getTo() != null && messageSent.getTo().length > 0) {
            assertEquals(user.getEmail(), messageSent.getTo()[0]);
        } else {
            fail("The 'To' field in the email is null or empty");
        }
        assertEquals("Password Reset Request", messageSent.getSubject());
        assertEquals("To reset your password, click the link below:\nhttp://localhost:8080/reset-password?token=" + token, messageSent.getText());
    }
}
