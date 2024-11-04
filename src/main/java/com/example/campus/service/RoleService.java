package com.example.campus.service;

import com.example.campus.entity.Permission;
import com.example.campus.entity.Role;
import com.example.campus.exception.PermissionNotFoundException;
import com.example.campus.exception.RoleNotFoundException;
import com.example.campus.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleService {
    private final PermissionService permissionService;
    private final RoleRepository roleRepository;

    public RoleService(
            PermissionService permissionService,
            RoleRepository roleRepository
    ) {
        this.permissionService = permissionService;
        this.roleRepository = roleRepository;
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findRoleById(Long roleId) throws RoleNotFoundException {
        return roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
    }

    public Role updateRole(Long roleId, Role roleDetails) throws RoleNotFoundException {
        Role role = findRoleById(roleId);
        if (role.getName() != null) role.setName(roleDetails.getName());
        return saveRole(role);
    }

    public void deleteRole(Long roleId) {
        log.info("Deleting role with id: {}", roleId);
        roleRepository.deleteById(roleId);
    }

    public List<Permission> getPermissionsByRoleId(Long roleId) throws RoleNotFoundException {
        Role role = findRoleById(roleId);
        return role.getPermissions();
    }

    public Role addPermissionToRole(Long roleId, Long permissionId) throws PermissionNotFoundException, RoleNotFoundException {
        Role role = findRoleById(roleId);
        Permission permission = permissionService.findPermissionById(permissionId);
        role.getPermissions().add(permission);
        return saveRole(role);
    }

    public Role removePermissionFromRole(Long roleId, Long permissionId) throws PermissionNotFoundException, RoleNotFoundException {
        Role role = findRoleById(roleId);
        Permission permission = permissionService.findPermissionById(permissionId);
        role.getPermissions().remove(permission);
        return saveRole(role);
    }
}
