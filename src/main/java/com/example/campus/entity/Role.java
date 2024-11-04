package com.example.campus.entity;

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
@Table(name = "roles")
public class Role extends Auditable {
    @NotBlank
    @Size(max = 100)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonIgnore
    private List<Permission> permissions = new ArrayList<>();

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<CourseRegistration> courseRegistrations = new ArrayList<>();

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users = new ArrayList<>();
}
