package com.example.campus.controller;

import com.example.campus.entity.Course;
import com.example.campus.service.CourseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Slf4j
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        log.info("Fetching all courses");
        List<Course> courses = courseService.findAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        log.info("Creating course with name: {}", course.getName());
        Course createdCourse = courseService.saveCourse(course);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Course>> getActiveCourses() {
        log.info("Fetching active courses");
        List<Course> courses = courseService.getActiveCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/open")
    public ResponseEntity<List<Course>> getOpenCourses() {
        log.info("Fetching open courses");
        List<Course> courses = courseService.getOpenCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        log.info("Fetching course with ID: {}", courseId);
        Course course = courseService.findCourseById(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long courseId, @Valid @RequestBody Course courseDetails) {
        log.info("Updating course with ID: {}", courseId);
        Course updatedCourse = courseService.updateCourse(courseId, courseDetails);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        log.info("Deleting course with ID: {}", courseId);
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{courseId}/open")
    public ResponseEntity<Course> openCourse(@PathVariable Long courseId) {
        log.info("Opening course with ID: {}", courseId);
        Course course = courseService.openCourse(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}/close")
    public ResponseEntity<Course> closeCourse(@PathVariable Long courseId) {
        log.info("Closing course with ID: {}", courseId);
        Course course = courseService.closeCourse(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}/activate")
    public ResponseEntity<Course> activateCourse(@PathVariable Long courseId) {
        log.info("Activating course with ID: {}", courseId);
        Course course = courseService.activateCourse(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{courseId}/deactivate")
    public ResponseEntity<Course> deactivateCourse(@PathVariable Long courseId) {
        log.info("Deactivating course with ID: {}", courseId);
        Course course = courseService.deactivateCourse(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}
