package com.example.campus.controller;

import com.example.campus.entity.Course;
import com.example.campus.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCourses() {
        List<Course> mockCourses = Collections.singletonList(new Course());

        when(courseService.findAllCourses()).thenReturn(mockCourses);

        ResponseEntity<List<Course>> response = courseController.getAllCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourses, response.getBody());
        verify(courseService, times(1)).findAllCourses();
    }

    @Test
    public void testCreateCourse() {
        Course mockCourse = new Course();
        Course createdCourse = new Course();

        when(courseService.saveCourse(mockCourse)).thenReturn(createdCourse);

        ResponseEntity<Course> response = courseController.createCourse(mockCourse);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCourse, response.getBody());
        verify(courseService, times(1)).saveCourse(mockCourse);
    }

    @Test
    public void testGetActiveCourses() {
        List<Course> mockActiveCourses = Collections.singletonList(new Course());

        when(courseService.getActiveCourses()).thenReturn(mockActiveCourses);

        ResponseEntity<List<Course>> response = courseController.getActiveCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockActiveCourses, response.getBody());
        verify(courseService, times(1)).getActiveCourses();
    }

    @Test
    public void testGetOpenCourses() {
        List<Course> mockOpenCourses = Collections.singletonList(new Course());

        when(courseService.getOpenCourses()).thenReturn(mockOpenCourses);

        ResponseEntity<List<Course>> response = courseController.getOpenCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOpenCourses, response.getBody());
        verify(courseService, times(1)).getOpenCourses();
    }

    @Test
    public void testGetCourseById() {
        Long courseId = 1L;
        Course mockCourse = new Course();

        when(courseService.findCourseById(courseId)).thenReturn(mockCourse);

        ResponseEntity<Course> response = courseController.getCourseById(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourse, response.getBody());
        verify(courseService, times(1)).findCourseById(courseId);
    }

    @Test
    public void testUpdateCourse() {
        Long courseId = 1L;
        Course courseDetails = new Course();
        Course updatedCourse = new Course();

        when(courseService.updateCourse(courseId, courseDetails)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.updateCourse(courseId, courseDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).updateCourse(courseId, courseDetails);
    }

    @Test
    public void testDeleteCourse() {
        Long courseId = 1L;

        ResponseEntity<Void> response = courseController.deleteCourse(courseId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseService, times(1)).deleteCourse(courseId);
    }

    @Test
    public void testOpenCourse() {
        Long courseId = 1L;
        Course updatedCourse = new Course();

        when(courseService.openCourse(courseId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.openCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).openCourse(courseId);
    }

    @Test
    public void testCloseCourse() {
        Long courseId = 1L;
        Course updatedCourse = new Course();

        when(courseService.closeCourse(courseId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.closeCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).closeCourse(courseId);
    }

    @Test
    public void testActivateCourse() {
        Long courseId = 1L;
        Course updatedCourse = new Course();

        when(courseService.activateCourse(courseId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.activateCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).activateCourse(courseId);
    }

    @Test
    public void testDeactivateCourse() {
        Long courseId = 1L;
        Course updatedCourse = new Course();

        when(courseService.deactivateCourse(courseId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.deactivateCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).deactivateCourse(courseId);
    }
}
