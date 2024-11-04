package com.example.campus.entity;

import com.example.campus.validation.ValidNationalId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Slf4j
public class User extends Auditable {
    @Column(length = 50, nullable = false)
    @NotBlank
    @Size(max = 50)
    private String name;

    @Column(name = "first_surname", length = 50, nullable = false)
    @NotBlank
    @Size(max = 50)
    private String firstSurname;

    @Column(name = "second_surname", length = 50)
    @Size(max = 50)
    private String secondSurname;

    @Column(nullable = false, length = 50)
    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @Embedded
    @ValidNationalId
    private NationalIdInfo nationalIdInfo;

    @Column(nullable = false, unique = true, length = 25)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String username;

    @Column(length = 60)
    @Size(min = 8, max = 60)
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<CourseRegistration> registrations = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private List<Role> roles = new ArrayList<>();

    @JsonIgnore
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        for (CourseRegistration registration : registrations) {
            courses.add(registration.getCourse());
        }
        return courses;
    }

    @JsonIgnore
    public List<Course> getActiveCourses() {
        List<Course> activeCourses = new ArrayList<>();
        for (CourseRegistration registration : registrations) {
            Course course = registration.getCourse();
            if (course.getIsActive()) {
                activeCourses.add(course);
            }
        }
        return activeCourses;
    }

    @JsonIgnore
    public List<Course> getOpenCourses() {
        List<Course> openCourses = new ArrayList<>();
        for (CourseRegistration registration : registrations) {
            Course course = registration.getCourse();
            if (course.getDates() != null && course.getDates().getStartDate().isBefore(LocalDateTime.now()) && course.getDates().getEndDate().isAfter(LocalDateTime.now())) {
                openCourses.add(course);
            }
        }
        return openCourses;
    }

    @JsonIgnore
    public List<Permission> getPermissions() {
        log.info("Fetching permissions for user: {}", this);
        List<Permission> permissions = new ArrayList<>();
        log.info("Fetching permissions for roles: {}", roles);
        for (Role role : roles) {
            log.info("Fetching permissions for role: {}", role);
            permissions.addAll(role.getPermissions());
        }
        for (CourseRegistration registration : registrations) {
            log.info("Fetching permissions for registration: {}", registration);
            Role role = registration.getRole();
            log.info("Fetching permissions for role: {}", role);
            permissions.addAll(role.getPermissions());
        }
        log.info("Returning permissions: {}", permissions);
        return permissions;
    }
}
