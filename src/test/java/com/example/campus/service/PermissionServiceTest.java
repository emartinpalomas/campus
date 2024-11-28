package com.example.campus.service;

import com.example.campus.entity.Permission;
import com.example.campus.exception.PermissionNotFoundException;
import com.example.campus.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PermissionServiceTest {

    private PermissionService permissionService;
    private PermissionRepository permissionRepository;

    @BeforeEach
    public void setUp() {
        permissionRepository = Mockito.mock(PermissionRepository.class);
        AuditableService auditableService = Mockito.mock(AuditableService.class);
        permissionService = new PermissionService(auditableService, permissionRepository);
    }

    @Test
    public void findAllPermissions() {
        Permission permission1 = new Permission();
        permission1.setId(1L);
        Permission permission2 = new Permission();
        permission2.setId(2L);
        List<Permission> permissions = List.of(permission1, permission2);

        when(permissionRepository.findAll()).thenReturn(permissions);

        List<Permission> result = permissionService.findAllPermissions();

        assertEquals(2, result.size());
        assertTrue(result.contains(permission1));
        assertTrue(result.contains(permission2));
    }

    @Test
    public void findPermissionById() throws PermissionNotFoundException {
        Permission permission = new Permission();
        permission.setId(1L);

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));

        Permission result = permissionService.findPermissionById(1L);

        assertEquals(1L, result.getId());

        when(permissionRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(PermissionNotFoundException.class, () -> permissionService.findPermissionById(2L));
    }

    @Test
    public void savePermission() {
        Permission permission = new Permission();
        permission.setId(1L);
        String requester = "requester";

        when(permissionRepository.save(permission)).thenReturn(permission);

        Permission result = permissionService.savePermission(requester, permission);

        assertEquals(1L, result.getId());
        verify(permissionRepository).save(permission);
    }

    @Test
    public void updatePermission() throws PermissionNotFoundException {
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setName("name");
        Permission permissionDetails = new Permission();
        permissionDetails.setName("new name");
        String requester = "requester";

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(permissionRepository.save(permission)).thenReturn(permission);

        Permission updatePermission = permissionService.updatePermission(requester, 1L, permissionDetails);

        assertEquals("new name", updatePermission.getName());
        verify(permissionRepository).save(permission);

        when(permissionRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(PermissionNotFoundException.class, () -> permissionService.updatePermission(requester, 2L, permissionDetails));
    }

    @Test
    public void deletePermission() {
        permissionService.deletePermission("requester", 1L);

        verify(permissionRepository).deleteById(1L);
    }
}
