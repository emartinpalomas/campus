package com.example.campus.service;

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

    private RoleService roleService;
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleService = new RoleService(roleRepository);
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

        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.saveRole(role);

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

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);

        Role updateRole = roleService.updateRole(1L, roleDetails);

        assertEquals("new name", updateRole.getName());
        verify(roleRepository).save(role);

        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.updateRole(2L, roleDetails));
    }

    @Test
    public void deleteRole() {
        roleService.deleteRole(1L);

        verify(roleRepository).deleteById(1L);
    }
}
