package com.example.campus.config;

import com.example.campus.repository.CourseRegistrationRepository;
import com.example.campus.repository.CourseRepository;
import com.example.campus.repository.UserRepository;
import com.example.campus.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CourseService courseService(
            AuditableService auditableService,
            CourseRepository courseRepository,
            CourseRegistrationRepository courseRegistrationRepository,
            RoleService roleService
    ) {
        return new CourseService(auditableService, courseRepository, courseRegistrationRepository, roleService);
    }

    @Bean
    public UserService userService(
            AuditableService auditableService,
            RoleService roleService,
            TextSanitizer textSanitizer,
            UserRepository userRepository,
            CourseService courseService
    ) {
        UserService userService = new UserService(auditableService, roleService, textSanitizer, userRepository);
        userService.setCourseService(courseService);
        courseService.setUserService(userService);
        return userService;
    }

    @Bean
    public TextSanitizer textSanitizer() {
        return new NonAsciiNormalizer();
    }
}
