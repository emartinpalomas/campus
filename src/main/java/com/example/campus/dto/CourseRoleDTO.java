package com.example.campus.dto;

import lombok.Data;

@Data
public class CourseRoleDTO {
    private Long courseId;
    private String courseName;
    private Long roleId;
    private String roleName;
}
