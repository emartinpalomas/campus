package com.example.campus.entity;

import com.example.campus.validation.EndDateAfterStartDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "courses")
public class Course extends Auditable {
    @NotBlank
    @Size(max = 100)
    private String name;

    @Embedded
    @EndDateAfterStartDate
    private DateRange dates;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    @ToString.Exclude
    private List<CourseRegistration> registrations = new ArrayList<>();

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean isActive = true;

    @JsonIgnore
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (CourseRegistration registration : registrations) {
            users.add(registration.getUser());
        }
        return users;
    }

    public List<User> getUsersWithRole(Long roleId) {
        List<User> users = new ArrayList<>();
        for (CourseRegistration registration : registrations) {
            if (registration.getRole().getId().equals(roleId)) {
                users.add(registration.getUser());
            }
        }
        return users;
    }

    public List<User> getUsersWithoutRole(Long roleId) {
        List<User> users = new ArrayList<>();
        for (CourseRegistration registration : registrations) {
            if (!registration.getRole().getId().equals(roleId)) {
                users.add(registration.getUser());
            }
        }
        return users;
    }
}
