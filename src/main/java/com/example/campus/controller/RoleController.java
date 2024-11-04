package com.example.campus.controller;

import com.example.campus.entity.Permission;
import com.example.campus.entity.Role;
import com.example.campus.service.RoleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Slf4j
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        log.info("Fetching all roles");
        List<Role> roles = roleService.findAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        log.info("Creating role with name: {}", role.getName());
        Role createdRole = roleService.saveRole(role);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long roleId) {
        log.info("Fetching role with ID: {}", roleId);
        Role role = roleService.findRoleById(roleId);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestBody Role roleDetails) {
        log.info("Updating role with ID: {}", roleId);
        Role updatedRole = roleService.updateRole(roleId, roleDetails);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        log.info("Deleting role with ID: {}", roleId);
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<List<Permission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        log.info("Fetching permissions for role with ID: {}", roleId);
        List<Permission> permissions = roleService.getPermissionsByRoleId(roleId);
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @PostMapping("/{roleId}/permission/{permissionId}")
    public ResponseEntity<Role> addPermissionToRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        log.info("Adding permission with ID: {} to role with ID: {}", permissionId, roleId);
        Role role = roleService.addPermissionToRole(roleId, permissionId);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}/permission/{permissionId}")
    public ResponseEntity<Role> removePermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        log.info("Removing permission with ID: {} from role with ID: {}", permissionId, roleId);
        Role role = roleService.removePermissionFromRole(roleId, permissionId);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
