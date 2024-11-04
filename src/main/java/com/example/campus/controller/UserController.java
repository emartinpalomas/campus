package com.example.campus.controller;

import com.example.campus.dto.CourseRoleDTO;
import com.example.campus.entity.Course;
import com.example.campus.entity.User;
import com.example.campus.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Creating user with username: {}", user.getUsername());
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        log.info("Fetching user with ID: {}", userId);
        User user = userService.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User userDetails) {
        log.info("Updating user with ID: {}", userId);
        User updatedUser = userService.updateUser(userId, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/courses")
    public ResponseEntity<List<Course>> getCoursesByUserId(@PathVariable Long userId) {
        log.info("Fetching courses for user ID: {}", userId);
        List<Course> courses = userService.getCoursesByUserId(userId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-active")
    public ResponseEntity<List<Course>> getActiveCoursesByUserId(@PathVariable Long userId) {
        log.info("Fetching active courses for user ID: {}", userId);
        List<Course> courses = userService.getActiveCoursesByUserId(userId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-open")
    public ResponseEntity<List<Course>> getOpenCoursesByUserId(@PathVariable Long userId) {
        log.info("Fetching open courses for user ID: {}", userId);
        List<Course> courses = userService.getOpenCoursesByUserId(userId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-roles")
    public ResponseEntity<List<CourseRoleDTO>> getCoursesAndRolesByUserId(@PathVariable Long userId) {
        log.info("Fetching courses and roles for user ID: {}", userId);
        List<CourseRoleDTO> courseRoleDTOs = userService.getCoursesAndRolesByUserId(userId);
        return new ResponseEntity<>(courseRoleDTOs, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-roles-active")
    public ResponseEntity<List<CourseRoleDTO>> getActiveCoursesAndRolesByUserId(@PathVariable Long userId) {
        log.info("Fetching active courses and roles for user ID: {}", userId);
        List<CourseRoleDTO> courseRoleDTOs = userService.getActiveCoursesAndRolesByUserId(userId);
        return new ResponseEntity<>(courseRoleDTOs, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-roles-open")
    public ResponseEntity<List<CourseRoleDTO>> getOpenCoursesAndRolesByUserId(@PathVariable Long userId) {
        log.info("Fetching open courses and roles for user ID: {}", userId);
        List<CourseRoleDTO> courseRoleDTOs = userService.getOpenCoursesAndRolesByUserId(userId);
        return new ResponseEntity<>(courseRoleDTOs, HttpStatus.OK);
    }

    @PostMapping("/{userId}/role/{roleId}")
    public ResponseEntity<User> addRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        log.info("Adding role ID: {} to user ID: {}", roleId, userId);
        User user = userService.addRoleToUser(userId, roleId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    public ResponseEntity<User> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        log.info("Removing role ID: {} from user ID: {}", roleId, userId);
        User user = userService.removeRoleFromUser(userId, roleId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<User> activateUser(@PathVariable Long userId) {
        log.info("Activating user with ID: {}", userId);
        User user = userService.activateUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long userId) {
        log.info("Deactivating user with ID: {}", userId);
        User user = userService.deactivateUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
