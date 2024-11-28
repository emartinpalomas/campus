package com.example.campus.controller;

import com.example.campus.entity.Permission;
import com.example.campus.service.PermissionService;
import com.example.campus.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static com.example.campus.util.Permissions.READ_PERMISSION;
import static com.example.campus.util.Permissions.WRITE_PERMISSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PermissionControllerTest {

    @InjectMocks
    private PermissionController permissionController;

    @Mock
    private PermissionService permissionService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testGetAllPermissions() {
        String requester = "testUser";
        List<Permission> mockPermissions = Collections.singletonList(new Permission());

        mockSecurityUtil(requester, Collections.singletonList(READ_PERMISSION.name()));
        when(permissionService.findAllPermissions()).thenReturn(mockPermissions);

        ResponseEntity<List<Permission>> response = permissionController.getAllPermissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPermissions, response.getBody());
        verify(permissionService, times(1)).findAllPermissions();
    }

    @Test
    public void testCreatePermission() {
        String requester = "testUser";
        Permission mockPermission = new Permission();
        Permission createdPermission = new Permission();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_PERMISSION.name()));
        when(permissionService.savePermission(requester, mockPermission)).thenReturn(createdPermission);

        ResponseEntity<Permission> response = permissionController.createPermission(mockPermission);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdPermission, response.getBody());
        verify(permissionService, times(1)).savePermission(requester, mockPermission);
    }

    @Test
    public void testGetPermissionById() {
        String requester = "testUser";
        Long permissionId = 1L;
        Permission mockPermission = new Permission();

        mockSecurityUtil(requester, Collections.singletonList(READ_PERMISSION.name()));
        when(permissionService.findPermissionById(permissionId)).thenReturn(mockPermission);

        ResponseEntity<Permission> response = permissionController.getPermissionById(permissionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPermission, response.getBody());
        verify(permissionService, times(1)).findPermissionById(permissionId);
    }

    @Test
    public void testUpdatePermission() {
        String requester = "testUser";
        Long permissionId = 1L;
        Permission permissionDetails = new Permission();
        Permission updatedPermission = new Permission();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_PERMISSION.name()));
        when(permissionService.updatePermission(requester, permissionId, permissionDetails)).thenReturn(updatedPermission);

        ResponseEntity<Permission> response = permissionController.updatePermission(permissionId, permissionDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPermission, response.getBody());
        verify(permissionService, times(1)).updatePermission(requester, permissionId, permissionDetails);
    }

    @Test
    public void testDeletePermission() {
        String requester = "testUser";
        Long permissionId = 1L;

        mockSecurityUtil(requester, Collections.singletonList(WRITE_PERMISSION.name()));

        ResponseEntity<Void> response = permissionController.deletePermission(permissionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(permissionService, times(1)).deletePermission(requester, permissionId);
    }

    private void mockSecurityUtil(String requester, List<String> permissions) {
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            when(authentication.getPrincipal()).thenReturn(requester);
            mockedSecurityUtil.when(SecurityUtil::getUsername).thenReturn(requester);
            mockedSecurityUtil.when(() -> SecurityUtil.isAuthorized(requester, permissions)).thenReturn(true);
        }
    }
}
