package com.example.campus.entity;

import com.example.campus.validation.ValidNationalId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
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
}
