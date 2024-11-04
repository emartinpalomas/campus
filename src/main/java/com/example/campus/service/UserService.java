package com.example.campus.service;

import com.example.campus.dto.CourseRoleDTO;
import com.example.campus.entity.*;
import com.example.campus.exception.*;
import com.example.campus.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    public static final int USERNAME_MAX_LENGTH = 20;
    public static final int MAX_RETRIES = 3;
    private final RoleService roleService;
    private final TextSanitizer textSanitizer;
    private final UserRepository userRepository;
    private CourseService courseService;

    public UserService(
            RoleService roleService,
            TextSanitizer textSanitizer,
            UserRepository userRepository
    ) {
        this.roleService = roleService;
        this.textSanitizer = textSanitizer;
        this.userRepository = userRepository;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) throws UserAlreadyExistsException, UserCreationFailedException {
        Optional<User> existingUser = userRepository.findByNationalIdInfo(user.getNationalIdInfo());
        if (existingUser.isPresent()) {
            log.error("Attempted to create user that already exists: {}", user);
            throw new UserAlreadyExistsException("User already exists");
        }
        String username = generateUsername(user);
        user.setUsername(username);
        log.info("User created with username: {}", username);

        int retryCount = 0;
        Throwable cause = null;
        while (retryCount < MAX_RETRIES) {
            try {
                return saveUser(user);
            } catch (DataIntegrityViolationException e) {
                cause = e.getCause();
                log.error("Data integrity violation: {}", cause != null ? cause.getMessage() : "Unknown cause");
                user.setUsername(generateUsername(user));
                retryCount++;
            }
        }
        throw new UserCreationFailedException("Failed to create user after " + MAX_RETRIES + " attempts", cause);
    }

    public User findUserById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    public User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    public User updateUser(Long userId, User userDetails) throws UserNotFoundException {
        User user = findUserById(userId);
        if (userDetails.getName() != null) user.setName(userDetails.getName());
        if (userDetails.getFirstSurname() != null) user.setFirstSurname(userDetails.getFirstSurname());
        if (userDetails.getSecondSurname() != null) user.setSecondSurname(userDetails.getSecondSurname());
        if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
        if (userDetails.getNationalIdInfo() != null) user.setNationalIdInfo(updateNationalIdInfo(userDetails));
        if (userDetails.getUsername() != null) user.setUsername(userDetails.getUsername());
        if (userDetails.getGender() != null) user.setGender(userDetails.getGender());
        if (userDetails.getIsActive() != null) user.setIsActive(userDetails.getIsActive());
        return saveUser(user);
    }

    public void deleteUser(Long userId) {
        log.info("Deleting user with id: {}", userId);
        userRepository.deleteById(userId);
    }

    public List<Course> getCoursesByUserId(Long userId) throws UserNotFoundException {
        User user = findUserById(userId);
        return user.getCourses();
    }

    public List<Course> getActiveCoursesByUserId(Long userId) throws UserNotFoundException {
        User user = findUserById(userId);
        return user.getActiveCourses();
    }

    public List<Course> getOpenCoursesByUserId(Long userId) throws UserNotFoundException {
        User user = findUserById(userId);
        return user.getOpenCourses();
    }

    public List<CourseRoleDTO> getCoursesAndRolesByUserId(Long userId) throws CourseNotFoundException, UserNotFoundException {
        List<Course> courses = getCoursesByUserId(userId);
        return getCourseRoleDTOS(userId, courses);
    }

    public List<CourseRoleDTO> getActiveCoursesAndRolesByUserId(Long userId) throws CourseNotFoundException, UserNotFoundException {
        List<Course> courses = getActiveCoursesByUserId(userId);
        return getCourseRoleDTOS(userId, courses);
    }

    public List<CourseRoleDTO> getOpenCoursesAndRolesByUserId(Long userId) throws CourseNotFoundException, UserNotFoundException {
        List<Course> courses = getOpenCoursesByUserId(userId);
        return getCourseRoleDTOS(userId, courses);
    }

    public User addRoleToUser(Long userId, Long roleId) throws RoleNotFoundException, UserNotFoundException {
        User user = findUserById(userId);
        Role role = roleService.findRoleById(roleId);
        user.getRoles().add(role);
        return saveUser(user);
    }

    public User removeRoleFromUser(Long userId, Long roleId) throws RoleNotFoundException, UserNotFoundException {
        User user = findUserById(userId);
        Role role = roleService.findRoleById(roleId);
        user.getRoles().remove(role);
        return saveUser(user);
    }

    public User activateUser(Long userId) throws UserNotFoundException {
        User user = findUserById(userId);
        user.setIsActive(true);
        return saveUser(user);
    }

    public User deactivateUser(Long userId) throws UserNotFoundException {
        User user = findUserById(userId);
        user.setIsActive(false);
        return saveUser(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    private String generateUsername(User user) {
        Set<String> allUsernames = new HashSet<>(userRepository.findAllUsernames());
        String name = textSanitizer.normalize(user.getName());
        String firstSurname = textSanitizer.normalize(user.getFirstSurname());
        String secondSurname = user.getSecondSurname();
        if (secondSurname != null) secondSurname = textSanitizer.normalize(secondSurname);

        String potentialUsername = obtainPotentialUsername(allUsernames, name, firstSurname, secondSurname, "");
        if (potentialUsername != null) return potentialUsername;

        int num = 1;
        while (true) {
            potentialUsername = obtainPotentialUsername(allUsernames, name, firstSurname, secondSurname, String.valueOf(num));
            if (potentialUsername != null) return potentialUsername;
            num++;
        }
    }

    private static String obtainPotentialUsername(Set<String> allUsernames, String name, String firstSurname, String secondSurname, String num) {
        for (int nameIndex = 1; nameIndex <= name.length(); nameIndex++) {
            if (secondSurname != null) {
                for (int secondSurnameIndex = 0; secondSurnameIndex <= secondSurname.length(); secondSurnameIndex++) {
                    StringBuilder potentialUsername = new StringBuilder(name.substring(0, nameIndex) + firstSurname + secondSurname.substring(0, secondSurnameIndex));
                    StringBuilder username = evaluatePotentialUsername(allUsernames, num, potentialUsername);
                    if (username != null) return username.toString();
                }
            } else {
                StringBuilder potentialUsername = new StringBuilder(name.substring(0, nameIndex) + firstSurname);
                StringBuilder username = evaluatePotentialUsername(allUsernames, num, potentialUsername);
                if (username != null) return username.toString();
            }
        }
        return null;
    }

    private static StringBuilder evaluatePotentialUsername(Set<String> allUsernames, String num, StringBuilder potentialUsername) {
        if (potentialUsername.length() > USERNAME_MAX_LENGTH) {
            potentialUsername = new StringBuilder(potentialUsername.substring(0, USERNAME_MAX_LENGTH));
        }
        if (!Objects.equals(num, "")) {
            potentialUsername.append(num);
        }
        if (!allUsernames.contains(potentialUsername.toString())) {
            return potentialUsername;
        }
        return null;
    }

    private static NationalIdInfo updateNationalIdInfo(User userDetails) {
        NationalIdInfo nationalIdInfo = new NationalIdInfo();
        if (userDetails.getNationalIdInfo().getNationalId() != null) {
            nationalIdInfo.setNationalId(userDetails.getNationalIdInfo().getNationalId());
        }
        if (userDetails.getNationalIdInfo().getCountry() != null) {
            nationalIdInfo.setCountry(userDetails.getNationalIdInfo().getCountry());
        }
        return nationalIdInfo;
    }

    private List<CourseRoleDTO> getCourseRoleDTOS(Long userId, List<Course> courses) throws CourseNotFoundException {
        return courses.stream().map(course -> {
            Optional<Role> role = courseService.getRoleByUserIdAndCourseId(userId, course.getId());
            CourseRoleDTO dto = new CourseRoleDTO();
            dto.setCourseId(course.getId());
            dto.setCourseName(course.getName());
            if (role.isPresent()) {
                dto.setRoleId(role.get().getId());
                dto.setRoleName(role.get().getName());
            } else {
                dto.setRoleId(null);
                dto.setRoleName("No Role");
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
