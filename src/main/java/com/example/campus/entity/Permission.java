package com.example.campus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "permissions")
public class Permission extends Auditable {
    @NotBlank
    @Size(max = 100)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();
}

