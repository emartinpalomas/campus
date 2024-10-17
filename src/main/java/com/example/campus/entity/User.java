package com.example.campus.entity;

import com.example.campus.validation.ValidNationalId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 50)
    @NotNull
    @Size(max = 50)
    private String name;

    @Column(name = "first_surname", length = 50)
    @NotNull
    @Size(max = 50)
    private String firstSurname;

    @Column(name = "second_surname", length = 50)
    @Size(max = 50)
    private String secondSurname;

    @Column(name = "email", nullable = false, length = 50)
    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @Column(name = "nationalId")
    @NotNull
    @ValidNationalId
    private String nationalId;

    @Column(name = "country")
    @NotNull
    private String country;

    @Column(name = "username", nullable = false, unique = true, length = 25)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String username;

    @Column(name = "password", length = 60)
    @Size(min = 8, max = 60)
    private String password;

    @Column(name = "gender")
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    @CreatedDate
    @PastOrPresent
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    @PastOrPresent
    private LocalDateTime updatedAt;
}
