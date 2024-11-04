package com.example.campus.service;

import com.example.campus.entity.Course;
import com.example.campus.entity.DateRange;
import com.example.campus.exception.CourseNotFoundException;
import com.example.campus.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServiceTest {

    private CourseService courseService;
    private CourseRepository courseRepository;

    @BeforeEach
    public void setup() {
        courseRepository = Mockito.mock(CourseRepository.class);
        courseService = new CourseService(courseRepository);
    }

    @Test
    public void testFindAllCourses() {
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(2L);
        List<Course> allCourses = List.of(course1, course2);

        when(courseRepository.findAll()).thenReturn(allCourses);

        List<Course> result = courseService.findAllCourses();

        assertEquals(2, result.size());
        assertTrue(result.contains(course1));
        assertTrue(result.contains(course2));
    }

    @Test
    public void testSaveCourse() {
        Course course = new Course();
        course.setId(1L);

        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseService.saveCourse(course);

        assertEquals(1L, result.getId());
        verify(courseRepository).save(course);
    }

    @Test
    public void testGetActiveCourses() {
        Course course1 = new Course();
        course1.setId(1L);
        course1.setIsActive(true);
        Course course2 = new Course();
        course2.setId(2L);
        course2.setIsActive(true);
        List<Course> activeCourses = List.of(course1, course2);

        when(courseRepository.findByIsActiveTrue()).thenReturn(activeCourses);

        List<Course> result = courseService.getActiveCourses();

        assertEquals(2, result.size());
        assertTrue(result.contains(course1));
        assertTrue(result.contains(course2));
    }

    @Test
    public void testGetOpenCourses() {
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(2L);
        List<Course> openCourses = List.of(course1, course2);

        when(courseRepository.findOpenCourses(any(LocalDateTime.class))).thenReturn(openCourses);

        List<Course> result = courseService.getOpenCourses();

        assertEquals(2, result.size());
        assertTrue(result.contains(course1));
        assertTrue(result.contains(course2));
    }

    @Test
    public void testFindCourseById() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseService.findCourseById(1L);

        assertEquals(1L, result.getId());

        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.findCourseById(2L));
    }

    @Test
    public void testUpdateCourse() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        Course courseDetails = new Course();
        courseDetails.setName("Updated Course");
        DateRange dates = new DateRange();
        dates.setStartDate(LocalDateTime.now().minusDays(1));
        dates.setEndDate(LocalDateTime.now().plusDays(1));
        courseDetails.setDates(dates);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updatedCourse = courseService.updateCourse(1L, courseDetails);

        assertEquals("Updated Course", updatedCourse.getName());
        assertEquals(LocalDateTime.now().minusDays(1).getDayOfYear(), updatedCourse.getDates().getStartDate().getDayOfYear());
        assertEquals(LocalDateTime.now().plusDays(1).getDayOfYear(), updatedCourse.getDates().getEndDate().getDayOfYear());
        verify(courseRepository).save(course);
    }

    @Test
    public void testDeleteCourse() {
        courseService.deleteCourse(1L);

        verify(courseRepository).deleteById(1L);
    }

    @Test
    public void testOpenCourse() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        DateRange dates = new DateRange();
        dates.setEndDate(LocalDateTime.now().minusDays(1));
        course.setDates(dates);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.openCourse(1L);

        assertEquals(LocalDateTime.now().getDayOfYear(), course.getDates().getStartDate().getDayOfYear());
        assertNull(course.getDates().getEndDate());
        verify(courseRepository).save(course);
    }

    @Test
    public void testCloseCourse() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        DateRange dates = new DateRange();
        dates.setStartDate(LocalDateTime.now().plusDays(1));
        course.setDates(dates);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.closeCourse(1L);

        assertEquals(LocalDateTime.now().getDayOfYear(), course.getDates().getEndDate().getDayOfYear());
        assertNull(course.getDates().getStartDate());
        verify(courseRepository).save(course);
    }

    @Test
    public void testActivateCourse() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        course.setIsActive(false);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.activateCourse(1L);

        assertTrue(course.getIsActive());
        verify(courseRepository).save(course);
    }

    @Test
    public void testDeactivateCourse() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        course.setIsActive(true);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deactivateCourse(1L);

        assertFalse(course.getIsActive());
        verify(courseRepository).save(course);
    }
}
