package com.example.campus.service;

import com.example.campus.dto.UserRoleDTO;
import com.example.campus.entity.*;
import com.example.campus.exception.CourseNotFoundException;
import com.example.campus.exception.RoleNotFoundException;
import com.example.campus.exception.UserNotFoundException;
import com.example.campus.repository.CourseRegistrationRepository;
import com.example.campus.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final RoleService roleService;
    private UserService userService;

    public CourseService(
            CourseRepository courseRepository,
            CourseRegistrationRepository courseRegistrationRepository,
            RoleService roleService
    ) {
        this.courseRepository = courseRepository;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.roleService = roleService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
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

    public List<User> getUsersByCourseId(Long courseId) throws CourseNotFoundException {
        Course course = findCourseById(courseId);
        return course.getUsers();
    }

    public Course addUserToCourse(Long courseId, Long userId, Long roleId) throws CourseNotFoundException, RoleNotFoundException, UserNotFoundException {
        Course course = findCourseById(courseId);
        User user = userService.findUserById(userId);
        Role role = roleService.findRoleById(roleId);
        CourseRegistration registration = new CourseRegistration();
        registration.setUser(user);
        registration.setRole(role);
        registration.setCourse(course);
        courseRegistrationRepository.save(registration);
        return saveCourse(course);
    }

    public Course removeUserFromCourse(Long courseId, Long userId) throws CourseNotFoundException, UserNotFoundException {
        Course course = findCourseById(courseId);
        User user = userService.findUserById(userId);
        CourseRegistration registration = course.getRegistrations().stream()
                .filter(reg -> reg.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found in course with id: " + courseId));
        course.getRegistrations().remove(registration);
        log.info("Deleting registration with id: {}", registration.getId());
        courseRegistrationRepository.delete(registration);
        return saveCourse(course);
    }

    public List<UserRoleDTO> getUsersAndRolesByCourseId(Long courseId) throws CourseNotFoundException {
        List<User> users = getUsersByCourseId(courseId);
        return getUserRoleDTOS(courseId, users);
    }

    public List<User> getUsersWithRole(Long courseId, Long roleId) throws CourseNotFoundException {
        Course course = findCourseById(courseId);
        return course.getUsersWithRole(roleId);
    }

    public List<User> getUsersWithoutRole(Long courseId, Long roleId) throws CourseNotFoundException {
        Course course = findCourseById(courseId);
        return course.getUsersWithoutRole(roleId);
    }

    public Optional<Role> getRoleByUserIdAndCourseId(Long userId, Long courseId) throws CourseNotFoundException, UserNotFoundException {
        Course course = findCourseById(courseId);
        User user = userService.findUserById(userId);
        return course.getRegistrations().stream()
                .filter(registration -> registration.getUser().equals(user))
                .map(CourseRegistration::getRole)
                .findFirst();
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

    private List<UserRoleDTO> getUserRoleDTOS(Long courseId, List<User> users) throws CourseNotFoundException, UserNotFoundException {
        return users.stream().map(user -> {
            Optional<Role> role = getRoleByUserIdAndCourseId(user.getId(), courseId);
            UserRoleDTO dto = new UserRoleDTO();
            dto.setUserId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setFirstSurname(user.getFirstSurname());
            dto.setSecondSurname(user.getSecondSurname());
            if (role.isPresent()) {
                dto.setRoleId(role.get().getId());
                dto.setRoleName(role.get().getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    private Course changeCourseActivationStatus(Long courseId, boolean isActive) {
        Course course = findCourseById(courseId);
        course.setIsActive(isActive);
        return saveCourse(course);
    }
}
