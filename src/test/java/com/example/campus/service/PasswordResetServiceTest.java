package com.example.campus.service;

import com.example.campus.entity.PasswordResetToken;
import com.example.campus.entity.User;
import com.example.campus.repository.PasswordResetTokenRepository;
import com.example.campus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @Mock
    private User mockUser;

    private PasswordResetService passwordResetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doAnswer(invocation -> {
            String arg = invocation.getArgument(0);
            when(mockUser.getPassword()).thenReturn(arg);
            return null;
        }).when(mockUser).setPassword(anyString());
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        passwordResetService = new PasswordResetService(userRepository, userService, passwordResetTokenRepository, passwordEncoder, mailService);
    }

    @Test
    public void testCreatePasswordResetTokenForUser() {
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        doAnswer(invocation -> {
            PasswordResetToken token = invocation.getArgument(0);
            assertEquals(mockUser, token.getUser());
            assertTrue(token.getExpiryDate().isAfter(LocalDateTime.now()));
            assertNotNull(token.getToken());
            return null;
        }).when(passwordResetTokenRepository).save(any(PasswordResetToken.class));

        passwordResetService.createPasswordResetTokenForUser(username);

        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(mailService, times(1)).sendPasswordResetMail(eq(mockUser), anyString());
    }

    @Test
    public void testValidatePasswordResetToken() {
        String token = "testToken";
        PasswordResetToken mockToken = mock(PasswordResetToken.class);

        when(mockToken.getExpiryDate()).thenReturn(LocalDateTime.now().plusHours(1));
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(mockToken);
        assertTrue(passwordResetService.validatePasswordResetToken(token));

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(null);
        assertFalse(passwordResetService.validatePasswordResetToken(token));

        when(mockToken.getExpiryDate()).thenReturn(LocalDateTime.now().minusHours(1));
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(mockToken);
        assertFalse(passwordResetService.validatePasswordResetToken(token));
    }

    @Test
    public void testResetPassword() {
        String token = "testToken";
        String newPassword = "newPassword";

        when(passwordResetTokenRepository.findByToken(anyString())).thenAnswer(invocation -> {
            PasswordResetToken passwordResetToken = mock(PasswordResetToken.class);
            when(passwordResetToken.getExpiryDate()).thenReturn(LocalDateTime.now().plusHours(1));
            when(passwordResetToken.getUser()).thenReturn(mockUser);
            return passwordResetToken;
        });

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.saveUser(userCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        passwordResetService.resetPassword(token, newPassword);

        User savedUser = userCaptor.getValue();
        verify(mockUser, times(1)).setPassword(anyString());
        assertEquals(newPassword, savedUser.getPassword());
        verify(userService, times(1)).saveUser(any(User.class));
        verify(passwordResetTokenRepository, times(1)).delete(any(PasswordResetToken.class));
    }
}
