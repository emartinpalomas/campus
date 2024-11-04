package com.example.campus.entity;

import com.example.campus.validation.EndDateAfterStartDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean isActive = true;
}
