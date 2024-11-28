package com.example.campus.service;

import com.example.campus.entity.Permission;
import com.example.campus.entity.Role;
import com.example.campus.exception.PermissionNotFoundException;
import com.example.campus.exception.RoleNotFoundException;
import com.example.campus.repository.RoleRepository;
import com.example.campus.util.EntitySaver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleService {
    private final AuditableService auditableService;
    private final PermissionService permissionService;
    private final RoleRepository roleRepository;

    public RoleService(
            AuditableService auditableService,
            PermissionService permissionService,
            RoleRepository roleRepository
    ) {
        this.auditableService = auditableService;
        this.permissionService = permissionService;
        this.roleRepository = roleRepository;
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Role saveRole(String requester, Role role) {
        return EntitySaver.saveEntity(auditableService, roleRepository, requester, role);
    }

    public Role findRoleById(Long roleId) throws RoleNotFoundException {
        return roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
    }

    public Role updateRole(String requester, Long roleId, Role roleDetails) throws RoleNotFoundException {
        Role role = findRoleById(roleId);
        if (role.getName() != null) role.setName(roleDetails.getName());
        return saveRole(requester, role);
    }

    public void deleteRole(String requester, Long roleId) {
        log.info("Deleting role with id: {} by user: {}", roleId, requester);
        roleRepository.deleteById(roleId);
    }

    public List<Permission> getPermissionsByRoleId(Long roleId) throws RoleNotFoundException {
        Role role = findRoleById(roleId);
        return role.getPermissions();
    }

    public Role addPermissionToRole(String requester, Long roleId, Long permissionId) throws PermissionNotFoundException, RoleNotFoundException {
        Role role = findRoleById(roleId);
        Permission permission = permissionService.findPermissionById(permissionId);
        role.getPermissions().add(permission);
        return saveRole(requester, role);
    }

    public Role removePermissionFromRole(String requester, Long roleId, Long permissionId) throws PermissionNotFoundException, RoleNotFoundException {
        Role role = findRoleById(roleId);
        Permission permission = permissionService.findPermissionById(permissionId);
        role.getPermissions().remove(permission);
        return saveRole(requester, role);
    }
}
