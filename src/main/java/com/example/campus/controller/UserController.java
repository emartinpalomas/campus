package com.example.campus.controller;

import com.example.campus.entity.User;
import com.example.campus.exception.UserAlreadyExistsException;
import com.example.campus.exception.UserCreationFailedException;
import com.example.campus.service.PasswordResetService;
import com.example.campus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.ConstraintViolationException;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordResetService passwordResetService;

    public UserController(
            UserService userService,
            PasswordResetService passwordResetService
    ) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (UserCreationFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .reduce((message1, message2) -> message1 + ", " + message2)
                .orElse("Validation error");
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<?> createPasswordResetToken(@RequestParam String username) {
        passwordResetService.createPasswordResetTokenForUser(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (passwordResetService.validatePasswordResetToken(token)) {
            passwordResetService.resetPassword(token, newPassword);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);
        }
    }
}
