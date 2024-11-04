package com.example.campus.dto;

import lombok.Data;

@Data
public class UserRoleDTO {
    private Long userId;
    private String username;
    private String name;
    private String firstSurname;
    private String secondSurname;
    private Long roleId;
    private String roleName;
}
