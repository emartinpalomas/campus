package com.example.campus.controller;

import com.example.campus.dto.CourseRoleDTO;
import com.example.campus.entity.Course;
import com.example.campus.entity.Permission;
import com.example.campus.entity.User;
import com.example.campus.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<User>> getAllUsers() {
        String requester = getRequester();
        log.info("Fetching all users requested by: {}", requester);
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_USER)")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        String requester = getRequester();
        log.info("Creating user with username: {} requested by: {}", user.getUsername(), requester);
        User createdUser = userService.createUser(requester, user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Fetching user with ID: {} requested by: {}", userId, requester);
        User user = userService.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_USER)")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User userDetails) {
        String requester = getRequester();
        log.info("Updating user with ID: {} requested by: {}", userId, requester);
        User updatedUser = userService.updateUser(requester, userId, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_USER)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Deleting user with ID: {} requested by: {}", userId, requester);
        userService.deleteUser(requester, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/courses")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<Course>> getCoursesByUserId(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Fetching courses for user ID: {} requested by: {}", userId, requester);
        List<Course> courses = userService.getCoursesByUserId(userId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-active")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<Course>> getActiveCoursesByUserId(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Fetching active courses for user ID: {} requested by: {}", userId, requester);
        List<Course> courses = userService.getActiveCoursesByUserId(userId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-open")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<Course>> getOpenCoursesByUserId(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Fetching open courses for user ID: {} requested by: {}", userId, requester);
        List<Course> courses = userService.getOpenCoursesByUserId(userId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-roles")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<CourseRoleDTO>> getCoursesAndRolesByUserId(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Fetching courses and roles for user ID: {} requested by: {}", userId, requester);
        List<CourseRoleDTO> courseRoleDTOs = userService.getCoursesAndRolesByUserId(userId);
        return new ResponseEntity<>(courseRoleDTOs, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-roles-active")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<CourseRoleDTO>> getActiveCoursesAndRolesByUserId(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Fetching active courses and roles for user ID: {} requested by: {}", userId, requester);
        List<CourseRoleDTO> courseRoleDTOs = userService.getActiveCoursesAndRolesByUserId(userId);
        return new ResponseEntity<>(courseRoleDTOs, HttpStatus.OK);
    }

    @GetMapping("/{userId}/courses-roles-open")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<CourseRoleDTO>> getOpenCoursesAndRolesByUserId(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Fetching open courses and roles for user ID: {} requested by: {}", userId, requester);
        List<CourseRoleDTO> courseRoleDTOs = userService.getOpenCoursesAndRolesByUserId(userId);
        return new ResponseEntity<>(courseRoleDTOs, HttpStatus.OK);
    }

    @GetMapping("/{userId}/permissions")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_PERMISSION) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<Permission>> getPermissionsByUserId(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Fetching permissions for user ID: {} requested by: {}", userId, requester);
        List<Permission> permissions = userService.getPermissionsByUserId(userId);
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @PostMapping("/{userId}/role/{roleId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE) and hasAuthority(T(com.example.campus.util.Permissions).WRITE_USER)")
    public ResponseEntity<User> addRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        String requester = getRequester();
        log.info("Adding role ID: {} to user ID: {} requested by: {}", roleId, userId, requester);
        User user = userService.addRoleToUser(requester, userId, roleId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE) and hasAuthority(T(com.example.campus.util.Permissions).WRITE_USER)")
    public ResponseEntity<User> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        String requester = getRequester();
        log.info("Removing role ID: {} from user ID: {} requested by: {}", roleId, userId, requester);
        User user = userService.removeRoleFromUser(requester, userId, roleId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}/activate")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_USER)")
    public ResponseEntity<User> activateUser(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Activating user with ID: {} requested by: {}", userId, requester);
        User user = userService.activateUser(requester, userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}/deactivate")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_USER)")
    public ResponseEntity<User> deactivateUser(@PathVariable Long userId) {
        String requester = getRequester();
        log.info("Deactivating user with ID: {} requested by: {}", userId, requester);
        User user = userService.deactivateUser(requester, userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
