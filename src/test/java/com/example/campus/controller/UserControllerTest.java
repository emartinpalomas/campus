package com.example.campus.controller;

import com.example.campus.dto.CourseRoleDTO;
import com.example.campus.entity.Course;
import com.example.campus.entity.Permission;
import com.example.campus.entity.User;
import com.example.campus.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<User> mockUsers = Collections.singletonList(new User());

        when(userService.findAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        User createdUser = new User();

        when(userService.createUser(user)).thenReturn(createdUser);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUser, response.getBody());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User mockUser = new User();

        when(userService.findUserById(userId)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).findUserById(userId);
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        User userDetails = new User();
        User updatedUser = new User();

        when(userService.updateUser(userId, userDetails)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(userId, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).updateUser(userId, userDetails);
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    public void testGetCoursesByUserId() {
        Long userId = 1L;
        List<Course> mockCourses = Collections.singletonList(new Course());

        when(userService.getCoursesByUserId(userId)).thenReturn(mockCourses);

        ResponseEntity<List<Course>> response = userController.getCoursesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourses, response.getBody());
        verify(userService, times(1)).getCoursesByUserId(userId);
    }

    @Test
    public void testGetActiveCoursesByUserId() {
        Long userId = 1L;
        List<Course> mockActiveCourses = Collections.singletonList(new Course());

        when(userService.getActiveCoursesByUserId(userId)).thenReturn(mockActiveCourses);

        ResponseEntity<List<Course>> response = userController.getActiveCoursesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockActiveCourses, response.getBody());
        verify(userService, times(1)).getActiveCoursesByUserId(userId);
    }

    @Test
    public void testGetOpenCoursesByUserId() {
        Long userId = 1L;
        List<Course> mockOpenCourses = Collections.singletonList(new Course());

        when(userService.getOpenCoursesByUserId(userId)).thenReturn(mockOpenCourses);

        ResponseEntity<List<Course>> response = userController.getOpenCoursesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOpenCourses, response.getBody());
        verify(userService, times(1)).getOpenCoursesByUserId(userId);
    }

    @Test
    public void testGetCoursesAndRolesByUserId() {
        Long userId = 1L;
        List<CourseRoleDTO> mockCourseRoles = Collections.singletonList(new CourseRoleDTO());

        when(userService.getCoursesAndRolesByUserId(userId)).thenReturn(mockCourseRoles);

        ResponseEntity<List<CourseRoleDTO>> response = userController.getCoursesAndRolesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourseRoles, response.getBody());
        verify(userService, times(1)).getCoursesAndRolesByUserId(userId);
    }

    @Test
    public void testGetActiveCoursesAndRolesByUserId() {
        Long userId = 1L;
        List<CourseRoleDTO> mockCourseRoles = Collections.singletonList(new CourseRoleDTO());

        when(userService.getActiveCoursesAndRolesByUserId(userId)).thenReturn(mockCourseRoles);

        ResponseEntity<List<CourseRoleDTO>> response = userController.getActiveCoursesAndRolesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourseRoles, response.getBody());
        verify(userService, times(1)).getActiveCoursesAndRolesByUserId(userId);
    }

    @Test
    public void testGetOpenCoursesAndRolesByUserId() {
        Long userId = 1L;
        List<CourseRoleDTO> mockCourseRoles = Collections.singletonList(new CourseRoleDTO());

        when(userService.getOpenCoursesAndRolesByUserId(userId)).thenReturn(mockCourseRoles);

        ResponseEntity<List<CourseRoleDTO>> response = userController.getOpenCoursesAndRolesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourseRoles, response.getBody());
        verify(userService, times(1)).getOpenCoursesAndRolesByUserId(userId);
    }

    @Test
    public void testGetPermissionsByUserId() {
        Long userId = 1L;
        List<Permission> mockPermissions = Collections.singletonList(new Permission());

        when(userService.getPermissionsByUserId(userId)).thenReturn(mockPermissions);

        ResponseEntity<List<Permission>> response = userController.getPermissionsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPermissions, response.getBody());
        verify(userService, times(1)).getPermissionsByUserId(userId);
    }

    @Test
    public void testAddRoleToUser() {
        Long userId = 1L;
        Long roleId = 2L;
        User updatedUser = new User();

        when(userService.addRoleToUser(userId, roleId)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.addRoleToUser(userId, roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).addRoleToUser(userId, roleId);
    }

    @Test
    public void testRemoveRoleFromUser() {
        Long userId = 1L;
        Long roleId = 2L;
        User updatedUser = new User();

        when(userService.removeRoleFromUser(userId, roleId)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.removeRoleFromUser(userId, roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).removeRoleFromUser(userId, roleId);
    }

    @Test
    public void testActivateUser() {
        Long userId = 1L;
        User updatedUser = new User();

        when(userService.activateUser(userId)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.activateUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).activateUser(userId);
    }

    @Test
    public void testDeactivateUser() {
        Long userId = 1L;
        User updatedUser = new User();

        when(userService.deactivateUser(userId)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.deactivateUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).deactivateUser(userId);
    }
}
