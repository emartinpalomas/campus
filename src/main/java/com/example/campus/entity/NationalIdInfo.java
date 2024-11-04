package com.example.campus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Embeddable
public class NationalIdInfo {

    @Column(name = "national_id", nullable = false)
    @NotBlank
    private String nationalId;

    @Column(nullable = false)
    @NotBlank
    private String country;
}
