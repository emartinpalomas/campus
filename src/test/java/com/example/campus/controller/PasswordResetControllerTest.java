package com.example.campus.controller;

import com.example.campus.service.PasswordResetService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PasswordResetControllerTest {

    @InjectMocks
    private PasswordResetController userController;

    @Mock
    private PasswordResetService passwordResetService;

    @Test
    public void testCreatePasswordResetToken() {
        String username = "jdoe";
        doNothing().when(passwordResetService).createPasswordResetTokenForUser(username);

        ResponseEntity<?> response = userController.createPasswordResetToken(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(passwordResetService, times(1)).createPasswordResetTokenForUser(username);
    }

    @Test
    public void testResetPasswordSuccess() {
        String token = "token";
        String newPassword = "newPassword";
        when(passwordResetService.validatePasswordResetToken(token)).thenReturn(true);
        doNothing().when(passwordResetService).resetPassword(token, newPassword);

        ResponseEntity<?> response = userController.resetPassword(token, newPassword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(passwordResetService, times(1)).resetPassword(token, newPassword);
    }
}