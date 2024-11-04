package com.example.campus.controller;

import com.example.campus.service.PasswordResetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-resets")
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/request/{username}")
    public ResponseEntity<?> createPasswordResetToken(@PathVariable String username) {
        passwordResetService.createPasswordResetTokenForUser(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset/{token}/{newPassword}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @PathVariable String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
