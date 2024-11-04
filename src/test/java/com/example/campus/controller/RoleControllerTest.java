package com.example.campus.controller;

import com.example.campus.entity.Role;
import com.example.campus.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRoles() {
        List<Role> mockRoles = Collections.singletonList(new Role());

        when(roleService.findAllRoles()).thenReturn(mockRoles);

        ResponseEntity<List<Role>> response = roleController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRoles, response.getBody());
        verify(roleService, times(1)).findAllRoles();
    }

    @Test
    public void testCreateRole() {
        Role mockRole = new Role();
        Role createdRole = new Role();

        when(roleService.saveRole(mockRole)).thenReturn(createdRole);

        ResponseEntity<Role> response = roleController.createRole(mockRole);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdRole, response.getBody());
        verify(roleService, times(1)).saveRole(mockRole);
    }

    @Test
    public void testGetRoleById() {
        Long roleId = 1L;
        Role mockRole = new Role();

        when(roleService.findRoleById(roleId)).thenReturn(mockRole);

        ResponseEntity<Role> response = roleController.getRoleById(roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRole, response.getBody());
        verify(roleService, times(1)).findRoleById(roleId);
    }

    @Test
    public void testUpdateRole() {
        Long roleId = 1L;
        Role roleDetails = new Role();
        Role updatedRole = new Role();

        when(roleService.updateRole(roleId, roleDetails)).thenReturn(updatedRole);

        ResponseEntity<Role> response = roleController.updateRole(roleId, roleDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRole, response.getBody());
        verify(roleService, times(1)).updateRole(roleId, roleDetails);
    }

    @Test
    public void testDeleteRole() {
        Long roleId = 1L;

        ResponseEntity<Void> response = roleController.deleteRole(roleId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roleService, times(1)).deleteRole(roleId);
    }
}
