package com.example.campus.controller;

import com.example.campus.dto.CourseRoleDTO;
import com.example.campus.entity.Course;
import com.example.campus.entity.Permission;
import com.example.campus.entity.User;
import com.example.campus.service.UserService;
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

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

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
    public void testGetAllUsers() {
        String requester = "testUser";
        List<User> mockUsers = Collections.singletonList(new User());

        mockSecurityUtil(requester, Collections.singletonList(READ_USER.name()));
        when(userService.findAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    public void testCreateUser() {
        String requester = "testUser";
        User user = new User();
        User createdUser = new User();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_USER.name()));
        when(userService.createUser(requester, user)).thenReturn(createdUser);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUser, response.getBody());
        verify(userService, times(1)).createUser(requester, user);
    }

    @Test
    public void testGetUserById() {
        String requester = "testUser";
        Long userId = 1L;
        User mockUser = new User();

        mockSecurityUtil(requester, Collections.singletonList(READ_USER.name()));
        when(userService.findUserById(userId)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).findUserById(userId);
    }

    @Test
    public void testUpdateUser() {
        String requester = "testUser";
        Long userId = 1L;
        User userDetails = new User();
        User updatedUser = new User();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_USER.name()));
        when(userService.updateUser(requester, userId, userDetails)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(userId, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).updateUser(requester, userId, userDetails);
    }

    @Test
    public void testDeleteUser() {
        String requester = "testUser";
        Long userId = 1L;

        mockSecurityUtil(requester, Collections.singletonList(WRITE_USER.name()));

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(requester, userId);
    }

    @Test
    public void testGetCoursesByUserId() {
        String requester = "testUser";
        Long userId = 1L;
        List<Course> mockCourses = Collections.singletonList(new Course());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_USER.name()));
        when(userService.getCoursesByUserId(userId)).thenReturn(mockCourses);

        ResponseEntity<List<Course>> response = userController.getCoursesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourses, response.getBody());
        verify(userService, times(1)).getCoursesByUserId(userId);
    }

    @Test
    public void testGetActiveCoursesByUserId() {
        String requester = "testUser";
        Long userId = 1L;
        List<Course> mockActiveCourses = Collections.singletonList(new Course());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_USER.name()));
        when(userService.getActiveCoursesByUserId(userId)).thenReturn(mockActiveCourses);

        ResponseEntity<List<Course>> response = userController.getActiveCoursesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockActiveCourses, response.getBody());
        verify(userService, times(1)).getActiveCoursesByUserId(userId);
    }

    @Test
    public void testGetOpenCoursesByUserId() {
        String requester = "testUser";
        Long userId = 1L;
        List<Course> mockOpenCourses = Collections.singletonList(new Course());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_USER.name()));
        when(userService.getOpenCoursesByUserId(userId)).thenReturn(mockOpenCourses);

        ResponseEntity<List<Course>> response = userController.getOpenCoursesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOpenCourses, response.getBody());
        verify(userService, times(1)).getOpenCoursesByUserId(userId);
    }

    @Test
    public void testGetCoursesAndRolesByUserId() {
        String requester = "testUser";
        Long userId = 1L;
        List<CourseRoleDTO> mockCourseRoles = Collections.singletonList(new CourseRoleDTO());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_ROLE.name(), READ_USER.name()));
        when(userService.getCoursesAndRolesByUserId(userId)).thenReturn(mockCourseRoles);

        ResponseEntity<List<CourseRoleDTO>> response = userController.getCoursesAndRolesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourseRoles, response.getBody());
        verify(userService, times(1)).getCoursesAndRolesByUserId(userId);
    }

    @Test
    public void testGetActiveCoursesAndRolesByUserId() {
        String requester = "testUser";
        Long userId = 1L;
        List<CourseRoleDTO> mockCourseRoles = Collections.singletonList(new CourseRoleDTO());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_ROLE.name(), READ_USER.name()));
        when(userService.getActiveCoursesAndRolesByUserId(userId)).thenReturn(mockCourseRoles);

        ResponseEntity<List<CourseRoleDTO>> response = userController.getActiveCoursesAndRolesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourseRoles, response.getBody());
        verify(userService, times(1)).getActiveCoursesAndRolesByUserId(userId);
    }

    @Test
    public void testGetOpenCoursesAndRolesByUserId() {
        String requester = "testUser";
        Long userId = 1L;
        List<CourseRoleDTO> mockCourseRoles = Collections.singletonList(new CourseRoleDTO());

        mockSecurityUtil(requester, Arrays.asList(READ_COURSE.name(), READ_ROLE.name(), READ_USER.name()));
        when(userService.getOpenCoursesAndRolesByUserId(userId)).thenReturn(mockCourseRoles);

        ResponseEntity<List<CourseRoleDTO>> response = userController.getOpenCoursesAndRolesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCourseRoles, response.getBody());
        verify(userService, times(1)).getOpenCoursesAndRolesByUserId(userId);
    }

    @Test
    public void testGetPermissionsByUserId() {
        String requester = "testUser";
        Long userId = 1L;
        List<Permission> mockPermissions = Collections.singletonList(new Permission());

        mockSecurityUtil(requester, Arrays.asList(READ_PERMISSION.name(), READ_USER.name()));
        when(userService.getPermissionsByUserId(userId)).thenReturn(mockPermissions);

        ResponseEntity<List<Permission>> response = userController.getPermissionsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPermissions, response.getBody());
        verify(userService, times(1)).getPermissionsByUserId(userId);
    }

    @Test
    public void testAddRoleToUser() {
        String requester = "testUser";
        Long userId = 1L;
        Long roleId = 2L;
        User updatedUser = new User();

        mockSecurityUtil(requester, Arrays.asList(READ_ROLE.name(), WRITE_USER.name()));
        when(userService.addRoleToUser(requester, userId, roleId)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.addRoleToUser(userId, roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).addRoleToUser(requester, userId, roleId);
    }

    @Test
    public void testRemoveRoleFromUser() {
        String requester = "testUser";
        Long userId = 1L;
        Long roleId = 2L;
        User updatedUser = new User();

        mockSecurityUtil(requester, Arrays.asList(READ_ROLE.name(), WRITE_USER.name()));
        when(userService.removeRoleFromUser(requester, userId, roleId)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.removeRoleFromUser(userId, roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).removeRoleFromUser(requester, userId, roleId);
    }

    @Test
    public void testActivateUser() {
        String requester = "testUser";
        Long userId = 1L;
        User updatedUser = new User();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_USER.name()));
        when(userService.activateUser(requester, userId)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.activateUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).activateUser(requester, userId);
    }

    @Test
    public void testDeactivateUser() {
        String requester = "testUser";
        Long userId = 1L;
        User updatedUser = new User();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_USER.name()));
        when(userService.deactivateUser(requester, userId)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.deactivateUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).deactivateUser(requester, userId);
    }

    private void mockSecurityUtil(String requester, List<String> permissions) {
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            when(authentication.getPrincipal()).thenReturn(requester);
            mockedSecurityUtil.when(SecurityUtil::getUsername).thenReturn(requester);
            mockedSecurityUtil.when(() -> SecurityUtil.isAuthorized(requester, permissions)).thenReturn(true);
        }
    }
}
