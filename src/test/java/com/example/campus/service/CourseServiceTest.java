package com.example.campus.service;

import com.example.campus.dto.UserRoleDTO;
import com.example.campus.entity.*;
import com.example.campus.exception.CourseNotFoundException;
import com.example.campus.exception.RoleNotFoundException;
import com.example.campus.exception.UserNotFoundException;
import com.example.campus.repository.CourseRegistrationRepository;
import com.example.campus.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServiceTest {

    private CourseService courseService;
    private CourseRepository courseRepository;
    private CourseRegistrationRepository courseRegistrationRepository;
    private UserService userService;
    private RoleService roleService;

    @BeforeEach
    public void setup() {
        courseRepository = Mockito.mock(CourseRepository.class);
        courseRegistrationRepository = Mockito.mock(CourseRegistrationRepository.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        AuditableService auditableService = Mockito.mock(AuditableService.class);
        courseService = new CourseService(auditableService, courseRepository, courseRegistrationRepository, roleService);
        courseService.setUserService(userService);
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
        String requester = "requester";

        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseService.saveCourse(requester, course);

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

        Course updatedCourse = courseService.updateCourse("requester", 1L, courseDetails);

        assertEquals("Updated Course", updatedCourse.getName());
        assertEquals(LocalDateTime.now().minusDays(1).getDayOfYear(), updatedCourse.getDates().getStartDate().getDayOfYear());
        assertEquals(LocalDateTime.now().plusDays(1).getDayOfYear(), updatedCourse.getDates().getEndDate().getDayOfYear());
        verify(courseRepository).save(course);
    }

    @Test
    public void testDeleteCourse() {
        courseService.deleteCourse("requester", 1L);

        verify(courseRepository).deleteById(1L);
    }

    @Test
    public void testGetUsersByCourseId() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);

        CourseRegistration registration1 = new CourseRegistration();
        registration1.setCourse(course);
        registration1.setUser(user1);

        CourseRegistration registration2 = new CourseRegistration();
        registration2.setCourse(course);
        registration2.setUser(user2);

        course.setRegistrations(new ArrayList<>(List.of(registration1, registration2)));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        List<User> users = courseService.getUsersByCourseId(1L);

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testAddUserToCourse() throws CourseNotFoundException, RoleNotFoundException, UserNotFoundException {
        Course course = new Course();
        course.setId(1L);
        User user = new User();
        user.setId(2L);
        Role role = new Role();
        role.setId(3L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.findUserById(2L)).thenReturn(user);
        when(roleService.findRoleById(3L)).thenReturn(role);

        courseService.addUserToCourse("requester", 1L, 2L, 3L);

        verify(courseRegistrationRepository).save(any(CourseRegistration.class));
        verify(courseRepository).save(course);
    }

    @Test
    public void testRemoveUserFromCourse() throws CourseNotFoundException, UserNotFoundException {
        Course course = new Course();
        course.setId(1L);
        User user = new User();
        user.setId(2L);

        CourseRegistration courseRegistration = new CourseRegistration();
        courseRegistration.setId(3L);
        courseRegistration.setCourse(course);
        courseRegistration.setUser(user);
        course.setRegistrations(new ArrayList<>(List.of(courseRegistration)));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.findUserById(2L)).thenReturn(user);
        doNothing().when(courseRegistrationRepository).deleteById(3L);

        courseService.removeUserFromCourse("requester", 1L, 2L);

        assertTrue(course.getRegistrations().isEmpty());
        verify(courseRegistrationRepository).delete(courseRegistration);
    }

    @Test
    public void testGetUsersAndRolesByCourseId() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        Role role = new Role();
        role.setId(2L);
        User user = new User();
        user.setId(3L);

        CourseRegistration courseRegistration = new CourseRegistration();
        courseRegistration.setCourse(course);
        courseRegistration.setRole(role);
        courseRegistration.setUser(user);
        course.setRegistrations(List.of(courseRegistration));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(roleService.findRoleById(2L)).thenReturn(role);
        when(userService.findUserById(3L)).thenReturn(user);

        List<UserRoleDTO> userRoles = courseService.getUsersAndRolesByCourseId(course.getId());

        assertEquals(1, userRoles.size());
        assertEquals(user.getId(), userRoles.get(0).getUserId());
        assertEquals(role.getId(), userRoles.get(0).getRoleId());
    }

    @Test
    public void testGetUsersWithRole() {
        Course course = new Course();
        course.setId(1L);
        Role role = new Role();
        role.setId(2L);
        User user1 = new User();
        user1.setId(3L);
        User user2 = new User();
        user2.setId(4L);

        CourseRegistration registration1 = new CourseRegistration();
        registration1.setCourse(course);
        registration1.setRole(role);
        registration1.setUser(user1);

        CourseRegistration registration2 = new CourseRegistration();
        registration2.setCourse(course);
        registration2.setRole(role);
        registration2.setUser(user2);

        course.setRegistrations(Arrays.asList(registration1, registration2));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(roleService.findRoleById(2L)).thenReturn(role);

        List<User> result = courseService.getUsersWithRole(1L, 2L);

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    public void testGetUsersWithoutRole() {
        Course course = new Course();
        course.setId(1L);
        Role role1 = new Role();
        role1.setId(2L);
        Role role2 = new Role();
        role2.setId(3L);
        User user1 = new User();
        user1.setId(4L);
        User user2 = new User();
        user2.setId(5L);

        CourseRegistration registration1 = new CourseRegistration();
        registration1.setCourse(course);
        registration1.setRole(role1);
        registration1.setUser(user1);

        CourseRegistration registration2 = new CourseRegistration();
        registration2.setCourse(course);
        registration2.setRole(role2);
        registration2.setUser(user2);

        course.setRegistrations(Arrays.asList(registration1, registration2));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(roleService.findRoleById(2L)).thenReturn(role1);

        List<User> result = courseService.getUsersWithoutRole(1L, 2L);

        assertEquals(1, result.size());
        assertTrue(result.contains(user2));
    }

    @Test
    public void testGetRoleByUserIdAndCourseId() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        Role role = new Role();
        role.setId(2L);
        User user = new User();
        user.setId(3L);

        CourseRegistration courseRegistration = new CourseRegistration();
        courseRegistration.setCourse(course);
        courseRegistration.setRole(role);
        courseRegistration.setUser(user);
        course.setRegistrations(List.of(courseRegistration));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(roleService.findRoleById(2L)).thenReturn(role);
        when(userService.findUserById(3L)).thenReturn(user);

        Optional<Role> result = courseService.getRoleByUserIdAndCourseId(user.getId(), course.getId());

        assertTrue(result.isPresent());
        assertEquals(role, result.get());
    }

    @Test
    public void testOpenCourse() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        DateRange dates = new DateRange();
        dates.setEndDate(LocalDateTime.now().minusDays(1));
        course.setDates(dates);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.openCourse("requester", 1L);

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

        courseService.closeCourse("requester", 1L);

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

        courseService.activateCourse("requester", 1L);

        assertTrue(course.getIsActive());
        verify(courseRepository).save(course);
    }

    @Test
    public void testDeactivateCourse() throws CourseNotFoundException {
        Course course = new Course();
        course.setId(1L);
        course.setIsActive(true);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deactivateCourse("requester", 1L);

        assertFalse(course.getIsActive());
        verify(courseRepository).save(course);
    }
}
