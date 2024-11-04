package com.example.campus.controller;

import com.example.campus.entity.Permission;
import com.example.campus.service.PermissionService;
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

public class PermissionControllerTest {

    @InjectMocks
    private PermissionController permissionController;

    @Mock
    private PermissionService permissionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPermissions() {
        List<Permission> mockPermissions = Collections.singletonList(new Permission());

        when(permissionService.findAllPermissions()).thenReturn(mockPermissions);

        ResponseEntity<List<Permission>> response = permissionController.getAllPermissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPermissions, response.getBody());
        verify(permissionService, times(1)).findAllPermissions();
    }

    @Test
    public void testCreatePermission() {
        Permission mockPermission = new Permission();
        Permission createdPermission = new Permission();

        when(permissionService.savePermission(mockPermission)).thenReturn(createdPermission);

        ResponseEntity<Permission> response = permissionController.createPermission(mockPermission);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdPermission, response.getBody());
        verify(permissionService, times(1)).savePermission(mockPermission);
    }

    @Test
    public void testGetPermissionById() {
        Long permissionId = 1L;
        Permission mockPermission = new Permission();

        when(permissionService.findPermissionById(permissionId)).thenReturn(mockPermission);

        ResponseEntity<Permission> response = permissionController.getPermissionById(permissionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPermission, response.getBody());
        verify(permissionService, times(1)).findPermissionById(permissionId);
    }

    @Test
    public void testUpdatePermission() {
        Long permissionId = 1L;
        Permission permissionDetails = new Permission();
        Permission updatedPermission = new Permission();

        when(permissionService.updatePermission(permissionId, permissionDetails)).thenReturn(updatedPermission);

        ResponseEntity<Permission> response = permissionController.updatePermission(permissionId, permissionDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPermission, response.getBody());
        verify(permissionService, times(1)).updatePermission(permissionId, permissionDetails);
    }

    @Test
    public void testDeletePermission() {
        Long permissionId = 1L;

        ResponseEntity<Void> response = permissionController.deletePermission(permissionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(permissionService, times(1)).deletePermission(permissionId);
    }
}
