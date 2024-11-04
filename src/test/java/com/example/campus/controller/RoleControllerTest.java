package com.example.campus.controller;

import com.example.campus.entity.Permission;
import com.example.campus.entity.Role;
import com.example.campus.service.RoleService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.campus.util.Permissions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

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
    public void testGetAllRoles() {
        String requester = "testUser";
        List<Role> mockRoles = Collections.singletonList(new Role());

        mockSecurityUtil(requester, Collections.singletonList(READ_ROLE.name()));
        when(roleService.findAllRoles()).thenReturn(mockRoles);

        ResponseEntity<List<Role>> response = roleController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRoles, response.getBody());
        verify(roleService, times(1)).findAllRoles();
    }

    @Test
    public void testCreateRole() {
        String requester = "testUser";
        Role mockRole = new Role();
        Role createdRole = new Role();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_ROLE.name()));
        when(roleService.saveRole(requester, mockRole)).thenReturn(createdRole);

        ResponseEntity<Role> response = roleController.createRole(mockRole);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdRole, response.getBody());
        verify(roleService, times(1)).saveRole(requester, mockRole);
    }

    @Test
    public void testGetRoleById() {
        String requester = "testUser";
        Long roleId = 1L;
        Role mockRole = new Role();

        mockSecurityUtil(requester, Collections.singletonList(READ_ROLE.name()));
        when(roleService.findRoleById(roleId)).thenReturn(mockRole);

        ResponseEntity<Role> response = roleController.getRoleById(roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRole, response.getBody());
        verify(roleService, times(1)).findRoleById(roleId);
    }

    @Test
    public void testUpdateRole() {
        String requester = "testUser";
        Long roleId = 1L;
        Role roleDetails = new Role();
        Role updatedRole = new Role();

        mockSecurityUtil(requester, Collections.singletonList(WRITE_ROLE.name()));
        when(roleService.updateRole(requester, roleId, roleDetails)).thenReturn(updatedRole);

        ResponseEntity<Role> response = roleController.updateRole(roleId, roleDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRole, response.getBody());
        verify(roleService, times(1)).updateRole(requester, roleId, roleDetails);
    }

    @Test
    public void testDeleteRole() {
        String requester = "testUser";
        Long roleId = 1L;

        mockSecurityUtil(requester, Collections.singletonList(WRITE_ROLE.name()));

        ResponseEntity<Void> response = roleController.deleteRole(roleId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roleService, times(1)).deleteRole(requester, roleId);
    }

    @Test
    public void testGetPermissionsByRoleId() {
        String requester = "testUser";
        Long roleId = 1L;
        List<Permission> mockPermissions = Collections.singletonList(new Permission());

        mockSecurityUtil(requester, Arrays.asList(READ_PERMISSION.name(), READ_ROLE.name()));
        when(roleService.getPermissionsByRoleId(roleId)).thenReturn(mockPermissions);

        ResponseEntity<List<Permission>> response = roleController.getPermissionsByRoleId(roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPermissions, response.getBody());
        verify(roleService, times(1)).getPermissionsByRoleId(roleId);
    }

    @Test
    public void testAddPermissionToRole() {
        String requester = "testUser";
        Long roleId = 1L;
        Long permissionId = 2L;
        Role updatedRole = new Role();

        mockSecurityUtil(requester, Arrays.asList(READ_PERMISSION.name(), WRITE_ROLE.name()));
        when(roleService.addPermissionToRole(requester, roleId, permissionId)).thenReturn(updatedRole);

        ResponseEntity<Role> response = roleController.addPermissionToRole(roleId, permissionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRole, response.getBody());
        verify(roleService, times(1)).addPermissionToRole(requester, roleId, permissionId);
    }

    @Test
    public void testRemovePermissionFromRole() {
        String requester = "testUser";
        Long roleId = 1L;
        Long permissionId = 2L;
        Role updatedRole = new Role();

        mockSecurityUtil(requester, Arrays.asList(READ_PERMISSION.name(), WRITE_ROLE.name()));
        when(roleService.removePermissionFromRole(requester, roleId, permissionId)).thenReturn(updatedRole);

        ResponseEntity<Role> response = roleController.removePermissionFromRole(roleId, permissionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRole, response.getBody());
        verify(roleService, times(1)).removePermissionFromRole(requester, roleId, permissionId);
    }

    private void mockSecurityUtil(String requester, List<String> permissions) {
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            when(authentication.getPrincipal()).thenReturn(requester);
            mockedSecurityUtil.when(SecurityUtil::getUsername).thenReturn(requester);
            mockedSecurityUtil.when(() -> SecurityUtil.isAuthorized(requester, permissions)).thenReturn(true);
        }
    }
}
