package com.example.campus.service;

import com.example.campus.entity.Permission;
import com.example.campus.entity.Role;
import com.example.campus.exception.RoleNotFoundException;
import com.example.campus.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoleServiceTest {

    private PermissionService permissionService;
    private RoleService roleService;
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        roleRepository = Mockito.mock(RoleRepository.class);
        permissionService = Mockito.mock(PermissionService.class);
        AuditableService auditableService = Mockito.mock(AuditableService.class);
        roleService = new RoleService(auditableService, permissionService, roleRepository);
    }

    @Test
    public void findAllRoles() {
        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);
        List<Role> roles = List.of(role1, role2);

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.findAllRoles();

        assertEquals(2, result.size());
        assertTrue(result.contains(role1));
        assertTrue(result.contains(role2));
    }

    @Test
    public void saveRole() {
        Role role = new Role();
        role.setId(1L);
        String requester = "requester";

        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.saveRole(requester, role);

        assertEquals(1L, result.getId());
        verify(roleRepository).save(role);
    }

    @Test
    public void findRoleById() throws RoleNotFoundException {
        Role role = new Role();
        role.setId(1L);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Role result = roleService.findRoleById(1L);

        assertEquals(1L, result.getId());

        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.findRoleById(2L));
    }

    @Test
    public void updateRole() throws RoleNotFoundException {
        Role role = new Role();
        role.setId(1L);
        role.setName("name");
        Role roleDetails = new Role();
        roleDetails.setName("new name");
        String requester = "requester";

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);

        Role updateRole = roleService.updateRole(requester, 1L, roleDetails);

        assertEquals("new name", updateRole.getName());
        verify(roleRepository).save(role);

        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.updateRole(requester, 2L, roleDetails));
    }

    @Test
    public void deleteRole() {
        roleService.deleteRole("requester", 1L);

        verify(roleRepository).deleteById(1L);
    }

    @Test
    public void getPermissionsByRoleId() {
        Permission permission1 = new Permission();
        permission1.setId(2L);
        Permission permission2 = new Permission();
        permission2.setId(3L);
        Role role = new Role();
        role.setId(1L);
        role.getPermissions().add(permission1);
        role.getPermissions().add(permission2);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        List<Permission> permissions = roleService.getPermissionsByRoleId(1L);

        assertEquals(2, permissions.size());
        assertTrue(permissions.contains(permission1));
        assertTrue(permissions.contains(permission2));
    }

    @Test
    public void addPermissionToRole() throws RoleNotFoundException {
        Role role = new Role();
        role.setId(1L);
        Permission permission = new Permission();
        permission.setId(2L);
        String requester = "requester";

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permissionService.findPermissionById(2L)).thenReturn(permission);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.addPermissionToRole(requester, 1L, 2L);

        assertTrue(result.getPermissions().contains(permission));
        verify(roleRepository).save(role);

        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.addPermissionToRole(requester, 2L, 2L));
    }

    @Test
    public void removePermissionFromRole() throws RoleNotFoundException {
        Role role = new Role();
        role.setId(1L);
        Permission permission = new Permission();
        permission.setId(2L);
        role.getPermissions().add(permission);
        String requester = "requester";

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permissionService.findPermissionById(2L)).thenReturn(permission);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.removePermissionFromRole(requester, 1L, 2L);

        assertFalse(result.getPermissions().contains(permission));
        verify(roleRepository).save(role);

        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.removePermissionFromRole(requester, 2L, 2L));
    }
}
