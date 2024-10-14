package com.example.campus.service;

import com.example.campus.entity.User;
import com.example.campus.repository.PasswordResetTokenRepository;
import com.example.campus.repository.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    private UserService userService;

    private Map<String, User> userDatabase = new HashMap<>();

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(
                userRepository,
                passwordResetTokenRepository,
                passwordEncoder,
                mailSender,
                templateEngine
        );
    }

    @ParameterizedTest
    @MethodSource("provideUsers")
    @Sql(statements = "INSERT INTO users (name, firstSurname, secondSurname) VALUES ('Jane', 'Doe', null)")
    public void testCreateUser(User user, String expectedUsername) {
        doAnswer(i -> Optional.ofNullable(userDatabase.get(i.getArgument(0))))
                .when(userRepository).findByUsername(anyString());
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User savedUser = i.getArgument(0);
            userDatabase.put(savedUser.getUsername(), savedUser);
            return savedUser;
        });

        User createdUser = userService.createUser(user);

        assertEquals(expectedUsername, createdUser.getUsername());
        verify(userRepository, times(3)).findByUsername(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    private static Stream<Arguments> provideUsers() {
        return Stream.of(
                Arguments.of(new User("Jane", "Doe", null), "jdoe"),
                Arguments.of(new User("John", "Doe", null), "jodoe"),
                Arguments.of(new User("Joel", "Doe", null), "joedoe"),
                Arguments.of(new User("Joe", "Doe", null), "joedoe1"),
                Arguments.of(new User("Joe", "Doe", null), "joedoe2"),
                Arguments.of(new User("Miquel", "Martí", "Pou"), "mmarti"),
                Arguments.of(new User("Maria", "Martí", "Pou"), "mmartip"),
                Arguments.of(new User("Manel", "Martí", "Pou"), "mmartipo"),
                Arguments.of(new User("Marcel", "Martí", "Pou"), "mmartipou"),
                Arguments.of(new User("Magí", "Martí", "Pou"), "mamartipou"),
                Arguments.of(new User("Marcel", "Martí", "Pou"), "marmartipou"),
                Arguments.of(new User("Marçal", "Martí", "Pou"), "marcamartipou"),
                Arguments.of(new User("Marc", "Martí", "Pou"), "marcmartipou1"),
                Arguments.of(new User("Sara", "Cañas", "Serrat"), "scanas"),
                Arguments.of(new User("Maria del Mar", "Vicenç", "Dalman"), "mdmvicenc"),
                Arguments.of(new User("Anna", "De la Torre", "Grau"), "adelatorre")
        );
    }
}
