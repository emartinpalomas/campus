package com.example.campus.service;

import com.example.campus.dto.CourseRoleDTO;
import com.example.campus.entity.*;
import com.example.campus.exception.CourseNotFoundException;
import com.example.campus.exception.UserNotFoundException;
import com.example.campus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
public class UserServiceTest {

    @Mock
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;
    private UserService userService;
    @Mock
    private CourseService courseService;

    @BeforeEach
    public void setup() {
        userService = new UserService(roleService, new DummyNormalizer(), userRepository);
        userService.setCourseService(courseService);
    }

    @Test
    public void testFindAllUsers() {
        User user1 = getUser();
        User user2 = getUser2();

        List<User> users = userService.findAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testCreateUser() {
        List<Arguments> usersAndUsernames = new ArrayList<>();
        usersAndUsernames.add(Arguments.of(createUser("Josep", "123450", "josep@example.com"), "JDoe"));
        usersAndUsernames.add(Arguments.of(createUser("Jane", "123451", "jane@example.com"), "JDoeP"));
        usersAndUsernames.add(Arguments.of(createUser("Jeremy", "123452", "jeremy@example.com"), "JDoePo"));
        usersAndUsernames.add(Arguments.of(createUser("Jana", "123453", "jana@example.com"), "JDoePou"));
        usersAndUsernames.add(Arguments.of(createUser("John", "123454", "john@example.com"), "JoDoe"));
        usersAndUsernames.add(Arguments.of(createUser("Joana", "123455", "joana@example.com"), "JoDoeP"));
        usersAndUsernames.add(Arguments.of(createUser("Joan", "123456", "joan@example.com"), "JoDoePo"));
        usersAndUsernames.add(Arguments.of(createUser("Jordi", "123457", "jordi@example.com"), "JoDoePou"));
        usersAndUsernames.add(Arguments.of(createUser("Joel", "123458", "joel@example.com"), "JoeDoe"));
        usersAndUsernames.add(Arguments.of(createUser("Joel Joan", "123459", "joeljoan@example.com"), "JoeDoeP"));
        usersAndUsernames.add(Arguments.of(createUser("Joey", "123460", "joey@example.com"), "JoeDoePo"));
        usersAndUsernames.add(Arguments.of(createUser("Joey Joan", "123461", "joeyjoan@example.com"), "JoeDoePou"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123462", "joe@example.com"), "JDoe1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123463", "joedoe@example.com"), "JDoeP1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123464", "joedoepou@example.com"), "JDoePo1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123465", "jdp@example.com"), "JDoePou1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123466", "joedp@example.com"), "JoDoe1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123467", "jdoe@example.com"), "JoDoeP1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123468", "jdoep@example.com"), "JoDoePo1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123469", "jdoepou@example.com"), "JoDoePou1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123470", "joe@example.com"), "JoeDoe1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123471", "joedoe@example.com"), "JoeDoeP1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123472", "joedoepou@example.com"), "JoeDoePo1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123473", "jdp@example.com"), "JoeDoePou1"));
        usersAndUsernames.add(Arguments.of(createUser("Joe", "123474", "joedp@example.com"), "JDoe2"));

        for (Arguments userAndUsername : usersAndUsernames) {
            User user = (User) userAndUsername.get()[0];
            String expectedUsername = (String) userAndUsername.get()[1];

            User createdUser = userService.createUser(user);

            assertEquals(expectedUsername, createdUser.getUsername());
        }
    }

    @Test
    public void testFindUserById() {
        User user = getUser();

        User foundUser = userService.findUserById(user.getId());

        assertEquals(user, foundUser);

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(999L));
    }

    @Test
    public void testFindUserByUsername() {
        User user = getUser();

        User foundUser = userService.findUserByUsername(user.getUsername());

        assertEquals(user, foundUser);

        assertThrows(UserNotFoundException.class, () -> userService.findUserByUsername("nonexistent"));
    }

    @Test
    public void testUpdateUser() {
        User user = getUser();

        User userDetails = new User();
        userDetails.setName("Alice Updated");

        User updatedUser = userService.updateUser(user.getId(), userDetails);

        assertEquals("Alice Updated", updatedUser.getName());
    }

    @Test
    public void testDeleteUser() {
        User user = getUser();

        userService.deleteUser(user.getId());

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    public void testGetCoursesByUserId() {
        User user = getUser();

        Course course1 = getCourse(1L, "Course 1", true);
        Course course2 = getCourse(2L, "Course 2", true);

        CourseRegistration registration1 = getCourseRegistration(user, course1, createRole());
        user.getRegistrations().add(registration1);

        CourseRegistration registration2 = getCourseRegistration(user, course2, createRole());
        user.getRegistrations().add(registration2);

        List<Course> courses = userService.getCoursesByUserId(user.getId());

        assertEquals(2, courses.size());
        assertTrue(courses.contains(course1));
        assertTrue(courses.contains(course2));
    }

    @Test
    public void testGetActiveCoursesByUserId() {
        User user = getUser();

        Course course1 = getCourse(1L, "Course 1", true);
        Course course2 = getCourse(2L, "Course 2", false);

        CourseRegistration registration1 = getCourseRegistration(user, course1, createRole());
        user.getRegistrations().add(registration1);

        CourseRegistration registration2 = getCourseRegistration(user, course2, createRole());
        user.getRegistrations().add(registration2);

        List<Course> courses = userService.getActiveCoursesByUserId(user.getId());

        assertEquals(1, courses.size());
        assertTrue(courses.contains(course1));
        assertFalse(courses.contains(course2));
    }

    @Test
    public void testGetOpenCoursesByUserId() {
        User user = getUser();

        Course course1 = getCourse(1L, "Course 1", true);
        course1.setDates(new DateRange());
        course1.getDates().setStartDate(LocalDateTime.now().minusDays(1));
        course1.getDates().setEndDate(LocalDateTime.now().plusDays(1));
        Course course2 = getCourse(2L, "Course 2", true);
        course2.setDates(new DateRange());
        course2.getDates().setStartDate(LocalDateTime.now().minusDays(2));
        course2.getDates().setEndDate(LocalDateTime.now().minusDays(1));
        Course course3 = getCourse(3L, "Course 3", true);
        course3.setDates(new DateRange());
        course3.getDates().setStartDate(LocalDateTime.now().plusDays(1));
        course3.getDates().setEndDate(LocalDateTime.now().plusDays(2));
        Course course4 = getCourse(4L, "Course 4", true);
        course4.setDates(new DateRange());
        course4.getDates().setStartDate(LocalDateTime.now().minusDays(1));
        course4.getDates().setEndDate(LocalDateTime.now().plusDays(1));

        CourseRegistration registration1 = getCourseRegistration(user, course1, createRole());
        user.getRegistrations().add(registration1);
        CourseRegistration registration2 = getCourseRegistration(user, course2, createRole());
        user.getRegistrations().add(registration2);
        CourseRegistration registration3 = getCourseRegistration(user, course3, createRole());
        user.getRegistrations().add(registration3);

        List<Course> courses = userService.getOpenCoursesByUserId(user.getId());

        assertEquals(1, courses.size());
        assertTrue(courses.contains(course1));
        assertFalse(courses.contains(course2));
        assertFalse(courses.contains(course3));
        assertFalse(courses.contains(course4));
    }

    @Test
    public void testGetCoursesAndRolesByUserId() throws UserNotFoundException, CourseNotFoundException {
        User user = getUser();

        Course course1 = getCourse(1L, "Course 1", true);
        Course course2 = getCourse(2L, "Course 2", true);

        CourseRegistration registration1 = getCourseRegistration(user, course1, createRole());
        user.getRegistrations().add(registration1);

        CourseRegistration registration2 = getCourseRegistration(user, course2, createRole2());
        user.getRegistrations().add(registration2);

        when(courseService.getRoleByUserIdAndCourseId(user.getId(), course1.getId())).thenReturn(Optional.of(createRole()));
        when(courseService.getRoleByUserIdAndCourseId(user.getId(), course2.getId())).thenReturn(Optional.of(createRole2()));

        List<CourseRoleDTO> courseRoleDTOs = userService.getCoursesAndRolesByUserId(user.getId());

        assertEquals(2, courseRoleDTOs.size());

        CourseRoleDTO dto1 = courseRoleDTOs.get(0);
        assertEquals(course1.getId(), dto1.getCourseId());
        assertEquals(course1.getName(), dto1.getCourseName());
        assertEquals(1L, dto1.getRoleId());
        assertEquals("Role 1", dto1.getRoleName());

        CourseRoleDTO dto2 = courseRoleDTOs.get(1);
        assertEquals(course2.getId(), dto2.getCourseId());
        assertEquals(course2.getName(), dto2.getCourseName());
        assertEquals(2L, dto2.getRoleId());
        assertEquals("Role 2", dto2.getRoleName());
    }

    @Test
    public void testGetActiveCoursesAndRolesByUserId() throws UserNotFoundException, CourseNotFoundException {
        User user = getUser();

        Course course1 = getCourse(1L, "Course 1", true);
        Course course2 = getCourse(2L, "Course 2", false);

        CourseRegistration registration1 = getCourseRegistration(user, course1, createRole());
        user.getRegistrations().add(registration1);

        CourseRegistration registration2 = getCourseRegistration(user, course2, createRole());
        user.getRegistrations().add(registration2);

        when(courseService.getRoleByUserIdAndCourseId(user.getId(), course1.getId())).thenReturn(Optional.of(createRole()));
        when(courseService.getRoleByUserIdAndCourseId(user.getId(), course2.getId())).thenReturn(Optional.of(createRole2()));

        List<CourseRoleDTO> courseRoleDTOs = userService.getActiveCoursesAndRolesByUserId(user.getId());

        assertEquals(1, courseRoleDTOs.size());

        CourseRoleDTO dto1 = courseRoleDTOs.get(0);
        assertEquals(course1.getId(), dto1.getCourseId());
        assertEquals(course1.getName(), dto1.getCourseName());
        assertEquals(1L, dto1.getRoleId());
        assertEquals("Role 1", dto1.getRoleName());
    }

    @Test
    public void testGetOpenCoursesAndRolesByUserId() {
        User user = getUser();

        Course course1 = getCourse(1L, "Course 1", true);
        course1.setDates(new DateRange());
        course1.getDates().setStartDate(LocalDateTime.now().minusDays(1));
        course1.getDates().setEndDate(LocalDateTime.now().plusDays(1));
        Course course2 = getCourse(2L, "Course 2", true);
        course2.setDates(new DateRange());
        course2.getDates().setStartDate(LocalDateTime.now().minusDays(2));
        course2.getDates().setEndDate(LocalDateTime.now().minusDays(1));
        Course course3 = getCourse(3L, "Course 3", true);
        course3.setDates(new DateRange());
        course3.getDates().setStartDate(LocalDateTime.now().plusDays(1));
        course3.getDates().setEndDate(LocalDateTime.now().plusDays(2));
        Course course4 = getCourse(4L, "Course 4", true);
        course4.setDates(new DateRange());
        course4.getDates().setStartDate(LocalDateTime.now().minusDays(1));
        course4.getDates().setEndDate(LocalDateTime.now().plusDays(1));

        CourseRegistration registration1 = getCourseRegistration(user, course1, createRole());
        user.getRegistrations().add(registration1);
        CourseRegistration registration2 = getCourseRegistration(user, course2, createRole());
        user.getRegistrations().add(registration2);
        CourseRegistration registration3 = getCourseRegistration(user, course3, createRole());
        user.getRegistrations().add(registration3);

        when(courseService.getRoleByUserIdAndCourseId(user.getId(), course1.getId())).thenReturn(Optional.of(createRole()));
        when(courseService.getRoleByUserIdAndCourseId(user.getId(), course2.getId())).thenReturn(Optional.of(createRole()));
        when(courseService.getRoleByUserIdAndCourseId(user.getId(), course3.getId())).thenReturn(Optional.of(createRole()));
        when(courseService.getRoleByUserIdAndCourseId(user.getId(), course4.getId())).thenReturn(Optional.of(createRole()));

        List<CourseRoleDTO> courseRoleDTOs = userService.getOpenCoursesAndRolesByUserId(user.getId());

        assertEquals(1, courseRoleDTOs.size());
        assertEquals(courseRoleDTOs.get(0).getCourseId(), course1.getId());
        assertEquals(courseRoleDTOs.get(0).getCourseName(), course1.getName());
        assertEquals(courseRoleDTOs.get(0).getRoleId(), 1L);
        assertEquals(courseRoleDTOs.get(0).getRoleName(), "Role 1");
    }

    @Test
    public void testActivateUser() {
        User user = getUser();
        user.setIsActive(false);
        userRepository.save(user);

        userService.activateUser(user.getId());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertTrue(updatedUser.getIsActive());
    }

    @Test
    public void testDeactivateUser() {
        User user = getUser();
        user.setIsActive(true);
        userRepository.save(user);

        userService.deactivateUser(user.getId());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertFalse(updatedUser.getIsActive());
    }

    @Test
    public void testSaveUser() {
        User user = getUser();
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals("Alice", savedUser.getName());
    }

    private User getUser() {
        User user = createUser("Alice", "123450", "alice@example.com");
        return userService.createUser(user);
    }

    private User getUser2() {
        User user = createUser("Bob", "123451", "bob@example.com");
        return userService.createUser(user);
    }

    private Role createRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Role 1");
        return role;
    }

    private Role createRole2() {
        Role role = new Role();
        role.setId(2L);
        role.setName("Role 2");
        return role;
    }

    private User createUser(String name, String nationalId, String email) {
        User user = new User();
        user.setName(name);
        user.setFirstSurname("Doe");
        user.setSecondSurname("Pou");
        user.setNationalIdInfo(createNationalIdInfo(nationalId));
        user.setEmail(email);
        user.setIsActive(true);
        return user;
    }

    private NationalIdInfo createNationalIdInfo(String nationalId) {
        NationalIdInfo nationalIdInfo = new NationalIdInfo();
        nationalIdInfo.setNationalId(nationalId);
        nationalIdInfo.setCountry("Canada");
        return nationalIdInfo;
    }

    private CourseRegistration getCourseRegistration(User user, Course course, Role role) {
        CourseRegistration registration1 = new CourseRegistration();
        registration1.setUser(user);
        registration1.setCourse(course);
        registration1.setRole(role);
        return registration1;
    }

    private Course getCourse(Long id, String name, Boolean active) {
        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setIsActive(active);
        course.setRegistrations(new ArrayList<>());
        return course;
    }
}
