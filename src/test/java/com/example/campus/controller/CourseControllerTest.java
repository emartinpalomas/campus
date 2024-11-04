package com.example.campus.controller;

import com.example.campus.dto.UserRoleDTO;
import com.example.campus.entity.Course;
import com.example.campus.entity.User;
import com.example.campus.service.CourseService;
import com.example.campus.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.campus.util.Permissions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testGetAllCourses() {
        String requester = "testUser";
        List<Course> mockCourses = Collections.singletonList(new Course());

        mockSecurityUtil(requester, Collections.singletonList(READ_COURSE.name()));
        when(courseService.findAllCourses()).thenReturn(mockCourses);

        ResponseEntity<List<Course>> response = courseController.getAllCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourses, response.getBody());
        verify(courseService, times(1)).findAllCourses();
    }

    @Test
    public void testCreateCourse() {
        String requester = "testUser";
        Course mockCourse = new Course();
        Course createdCourse = new Course();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_COURSE.name()));
        when(courseService.saveCourse(requester, mockCourse)).thenReturn(createdCourse);

        ResponseEntity<Course> response = courseController.createCourse(mockCourse);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCourse, response.getBody());
        verify(courseService, times(1)).saveCourse(requester, mockCourse);
    }

    @Test
    public void testGetActiveCourses() {
        String requester = "testUser";
        List<Course> mockActiveCourses = Collections.singletonList(new Course());

        mockSecurityUtil(requester, Collections.singletonList(READ_COURSE.name()));
        when(courseService.getActiveCourses()).thenReturn(mockActiveCourses);

        ResponseEntity<List<Course>> response = courseController.getActiveCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockActiveCourses, response.getBody());
        verify(courseService, times(1)).getActiveCourses();
    }

    @Test
    public void testGetOpenCourses() {
        String requester = "testUser";
        List<Course> mockOpenCourses = Collections.singletonList(new Course());

        mockSecurityUtil(requester, Collections.singletonList(READ_COURSE.name()));
        when(courseService.getOpenCourses()).thenReturn(mockOpenCourses);

        ResponseEntity<List<Course>> response = courseController.getOpenCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOpenCourses, response.getBody());
        verify(courseService, times(1)).getOpenCourses();
    }

    @Test
    public void testGetCourseById() {
        String requester = "testUser";
        Long courseId = 1L;
        Course mockCourse = new Course();

        mockSecurityUtil(requester, Collections.singletonList(READ_COURSE.name()));
        when(courseService.findCourseById(courseId)).thenReturn(mockCourse);

        ResponseEntity<Course> response = courseController.getCourseById(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourse, response.getBody());
        verify(courseService, times(1)).findCourseById(courseId);
    }

    @Test
    public void testUpdateCourse() {
        String requester = "testUser";
        Long courseId = 1L;
        Course courseDetails = new Course();
        Course updatedCourse = new Course();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_COURSE.name()));
        when(courseService.updateCourse(requester, courseId, courseDetails)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.updateCourse(courseId, courseDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).updateCourse(requester, courseId, courseDetails);
    }

    @Test
    public void testDeleteCourse() {
        String requester = "testUser";
        Long courseId = 1L;

        mockSecurityUtil(requester, Collections.singletonList(WRITE_COURSE.name()));

        ResponseEntity<Void> response = courseController.deleteCourse(courseId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseService, times(1)).deleteCourse(requester, courseId);
    }

    @Test
    public void testAddUserToCourse() {
        String requester = "testUser";
        Long courseId = 1L;
        Long userId = 2L;
        Long roleId = 3L;
        Course updatedCourse = new Course();

        mockSecurityUtil(requester, Arrays.asList(READ_USER.name(), WRITE_COURSE.name()));
        when(courseService.addUserToCourse(requester, courseId, userId, roleId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.addUserToCourse(courseId, userId, roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).addUserToCourse(requester, courseId, userId, roleId);
    }

    @Test
    public void testRemoveUserFromCourse() {
        String requester = "testUser";
        Long courseId = 1L;
        Long userId = 2L;
        Course updatedCourse = new Course();

        mockSecurityUtil(requester, Arrays.asList(READ_USER.name(), WRITE_COURSE.name()));
        when(courseService.removeUserFromCourse(requester, courseId, userId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.removeUserFromCourse(courseId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).removeUserFromCourse(requester, courseId, userId);
    }

    @Test
    public void testGetUsersByCourseId() {
        String requester = "testUser";
        Long courseId = 1L;
        List<User> mockUsers = Collections.singletonList(new User());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_USER.name()));
        when(courseService.getUsersByCourseId(courseId)).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = courseController.getUsersByCourseId(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        verify(courseService, times(1)).getUsersByCourseId(courseId);
    }

    @Test
    public void testGetUsersAndRolesByCourseId() {
        String requester = "testUser";
        Long courseId = 1L;
        List<UserRoleDTO> mockUserRoles = Collections.singletonList(new UserRoleDTO());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_ROLE.name(), READ_USER.name()));
        when(courseService.getUsersAndRolesByCourseId(courseId)).thenReturn(mockUserRoles);

        ResponseEntity<List<UserRoleDTO>> response = courseController.getUsersAndRolesByCourseId(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserRoles, response.getBody());
        verify(courseService, times(1)).getUsersAndRolesByCourseId(courseId);
    }

    @Test
    public void testGetUsersWithRole() {
        String requester = "testUser";
        Long courseId = 1L;
        Long roleId = 2L;
        List<User> mockUsers = Collections.singletonList(new User());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_ROLE.name(), READ_USER.name()));
        when(courseService.getUsersWithRole(courseId, roleId)).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = courseController.getUsersWithRole(courseId, roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        verify(courseService, times(1)).getUsersWithRole(courseId, roleId);
    }

    @Test
    public void testGetUsersWithoutRole() {
        String requester = "testUser";
        Long courseId = 1L;
        Long roleId = 2L;
        List<User> mockUsers = Collections.singletonList(new User());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_ROLE.name(), READ_USER.name()));
        when(courseService.getUsersWithoutRole(courseId, roleId)).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = courseController.getUsersWithoutRole(courseId, roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        verify(courseService, times(1)).getUsersWithoutRole(courseId, roleId);
    }

    @Test
    public void testOpenCourse() {
        String requester = "testUser";
        Long courseId = 1L;
        Course updatedCourse = new Course();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_COURSE.name()));
        when(courseService.openCourse(requester, courseId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.openCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).openCourse(requester, courseId);
    }

    @Test
    public void testCloseCourse() {
        String requester = "testUser";
        Long courseId = 1L;
        Course updatedCourse = new Course();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_COURSE.name()));
        when(courseService.closeCourse(requester, courseId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.closeCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).closeCourse(requester, courseId);
    }

    @Test
    public void testActivateCourse() {
        String requester = "testUser";
        Long courseId = 1L;
        Course updatedCourse = new Course();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_COURSE.name()));
        when(courseService.activateCourse(requester, courseId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.activateCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).activateCourse(requester, courseId);
    }

    @Test
    public void testDeactivateCourse() {
        String requester = "testUser";
        Long courseId = 1L;
        Course updatedCourse = new Course();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_COURSE.name()));
        when(courseService.deactivateCourse(requester, courseId)).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.deactivateCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService, times(1)).deactivateCourse(requester, courseId);
    }

    private void mockSecurityUtil(String requester, List<String> permissions) {
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            when(authentication.getPrincipal()).thenReturn(requester);
            mockedSecurityUtil.when(SecurityUtil::getUsername).thenReturn(requester);
            mockedSecurityUtil.when(() -> SecurityUtil.isAuthorized(requester, permissions)).thenReturn(true);
        }
    }
}
