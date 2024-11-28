package com.example.campus.controller;

import com.example.campus.dto.UserRoleDTO;
import com.example.campus.entity.Course;
import com.example.campus.entity.User;
import com.example.campus.service.CourseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Slf4j
public class CourseController extends BaseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE)")
    public ResponseEntity<List<Course>> getAllCourses() {
        String requester = getRequester();
        log.info("Fetching all courses requested by: {}", requester);
        List<Course> courses = courseService.findAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_COURSE)")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        String requester = getRequester();
        log.info("Creating course with name: {} requested by: {}", course.getName(), requester);
        Course createdCourse = courseService.saveCourse(requester, course);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE)")
    public ResponseEntity<List<Course>> getActiveCourses() {
        String requester = getRequester();
        log.info("Fetching active courses requested by: {}", requester);
        List<Course> courses = courseService.getActiveCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/open")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE)")
    public ResponseEntity<List<Course>> getOpenCourses() {
        String requester = getRequester();
        log.info("Fetching open courses requested by: {}", requester);
        List<Course> courses = courseService.getOpenCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE)")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        String requester = getRequester();
        log.info("Fetching course with ID: {} requested by: {}", courseId, requester);
        Course course = courseService.findCourseById(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_COURSE)")
    public ResponseEntity<Course> updateCourse(@PathVariable Long courseId, @Valid @RequestBody Course courseDetails) {
        String requester = getRequester();
        log.info("Updating course with ID: {} requested by: {}", courseId, requester);
        Course updatedCourse = courseService.updateCourse(requester, courseId, courseDetails);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_COURSE)")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        String requester = getRequester();
        log.info("Deleting course with ID: {} requested by: {}", courseId, requester);
        courseService.deleteCourse(requester, courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}/users")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<User>> getUsersByCourseId(@PathVariable Long courseId) {
        String requester = getRequester();
        log.info("Fetching users for course ID: {} requested by: {}", courseId, requester);
        List<User> users = courseService.getUsersByCourseId(courseId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/{courseId}/user/{userId}/role/{roleId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_USER) and hasAuthority(T(com.example.campus.util.Permissions).WRITE_COURSE)")
    public ResponseEntity<Course> addUserToCourse(@PathVariable Long courseId, @PathVariable Long userId, @PathVariable Long roleId) {
        String requester = getRequester();
        log.info("Adding user ID: {} with role ID: {} to course ID: {} requested by: {}", userId, roleId, courseId, requester);
        Course course = courseService.addUserToCourse(requester, courseId, userId, roleId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}/user/{userId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_USER) and hasAuthority(T(com.example.campus.util.Permissions).WRITE_COURSE)")
    public ResponseEntity<Course> removeUserFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        String requester = getRequester();
        log.info("Removing user ID: {} from course ID: {} requested by: {}", userId, courseId, requester);
        Course course = courseService.removeUserFromCourse(requester, courseId, userId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("/{courseId}/users-roles")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<UserRoleDTO>> getUsersAndRolesByCourseId(@PathVariable Long courseId) {
        String requester = getRequester();
        log.info("Fetching users and roles for course ID: {} requested by: {}", courseId, requester);
        List<UserRoleDTO> userRoleDTOs = courseService.getUsersAndRolesByCourseId(courseId);
        return new ResponseEntity<>(userRoleDTOs, HttpStatus.OK);
    }

    @GetMapping("/{courseId}/users-with-role/{roleId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<User>> getUsersWithRole(@PathVariable Long courseId, @PathVariable Long roleId) {
        String requester = getRequester();
        log.info("Fetching users with role ID: {} for course ID: {} requested by: {}", roleId, courseId, requester);
        List<User> users = courseService.getUsersWithRole(courseId, roleId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{courseId}/users-without-role/{roleId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_COURSE) and hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE) and hasAuthority(T(com.example.campus.util.Permissions).READ_USER)")
    public ResponseEntity<List<User>> getUsersWithoutRole(@PathVariable Long courseId, @PathVariable Long roleId) {
        String requester = getRequester();
        log.info("Fetching users without role ID: {} for course ID: {} requested by: {}", roleId, courseId, requester);
        List<User> users = courseService.getUsersWithoutRole(courseId, roleId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{courseId}/open")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_COURSE)")
    public ResponseEntity<Course> openCourse(@PathVariable Long courseId) {
        String requester = getRequester();
        log.info("Opening course with ID: {} requested by: {}", courseId, requester);
        Course course = courseService.openCourse(requester, courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}/close")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_COURSE)")
    public ResponseEntity<Course> closeCourse(@PathVariable Long courseId) {
        String requester = getRequester();
        log.info("Closing course with ID: {} requested by: {}", courseId, requester);
        Course course = courseService.closeCourse(requester, courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}/activate")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_COURSE)")
    public ResponseEntity<Course> activateCourse(@PathVariable Long courseId) {
        String requester = getRequester();
        log.info("Activating course with ID: {} requested by: {}", courseId, requester);
        Course course = courseService.activateCourse(requester, courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}/deactivate")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_COURSE)")
    public ResponseEntity<Course> deactivateCourse(@PathVariable Long courseId) {
        String requester = getRequester();
        log.info("Deactivating course with ID: {} requested by: {}", courseId, requester);
        Course course = courseService.deactivateCourse(requester, courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}
