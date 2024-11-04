package com.example.campus.service;

import com.example.campus.entity.Course;
import com.example.campus.entity.DateRange;
import com.example.campus.exception.CourseNotFoundException;
import com.example.campus.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getActiveCourses() {
        return courseRepository.findByIsActiveTrue();
    }

    public List<Course> getOpenCourses() {
        return courseRepository.findOpenCourses(LocalDateTime.now());
    }

    public Course findCourseById(Long courseId) throws CourseNotFoundException {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));
    }

    public Course updateCourse(Long courseId, Course courseDetails) throws CourseNotFoundException {
        Course course = findCourseById(courseId);
        updateCourseDetails(courseDetails, course);
        return saveCourse(course);
    }

    public void deleteCourse(Long courseId) {
        log.info("Deleting course with id: {}", courseId);
        courseRepository.deleteById(courseId);
    }

    public Course openCourse(Long courseId) throws CourseNotFoundException {
        Course course = findCourseById(courseId);
        DateRange dates = course.getDates();
        dates.setStartDate(LocalDateTime.now());
        if (dates.getEndDate() != null && dates.getEndDate().isBefore(dates.getStartDate())) {
            dates.setEndDate(null);
        }
        course.setDates(dates);
        return saveCourse(course);
    }

    public Course closeCourse(Long courseId) throws CourseNotFoundException {
        Course course = findCourseById(courseId);
        DateRange dates = course.getDates();
        dates.setEndDate(LocalDateTime.now());
        if (dates.getStartDate() != null && dates.getStartDate().isAfter(dates.getEndDate())) {
            dates.setStartDate(null);
        }
        course.setDates(dates);
        return saveCourse(course);
    }

    public Course activateCourse(Long courseId) throws CourseNotFoundException {
        return changeCourseActivationStatus(courseId, true);
    }

    public Course deactivateCourse( Long courseId) throws CourseNotFoundException {
        return changeCourseActivationStatus(courseId, false);
    }

    private void updateCourseDetails(Course courseDetails, Course course) {
        if (courseDetails.getName() != null) course.setName(courseDetails.getName());
        if (courseDetails.getDates() != null) course.setDates(updateDates(courseDetails));
        if (courseDetails.getIsActive() != null) course.setIsActive(courseDetails.getIsActive());
    }

    private DateRange updateDates(Course courseDetails) {
        DateRange dates = new DateRange();
        if (courseDetails.getDates().getStartDate() != null) dates.setStartDate(courseDetails.getDates().getStartDate());
        if (courseDetails.getDates().getEndDate() != null) dates.setEndDate(courseDetails.getDates().getEndDate());
        return dates;
    }

    private Course changeCourseActivationStatus(Long courseId, boolean isActive) {
        Course course = findCourseById(courseId);
        course.setIsActive(isActive);
        return saveCourse(course);
    }
}
