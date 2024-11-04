package com.example.campus.controller;

import com.example.campus.entity.Permission;
import com.example.campus.entity.Role;
import com.example.campus.service.RoleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Slf4j
public class RoleController extends BaseController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE)")
    public ResponseEntity<List<Role>> getAllRoles() {
        String requester = getRequester();
        log.info("Fetching all roles requested by: {}", requester);
        List<Role> roles = roleService.findAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_ROLE)")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        String requester = getRequester();
        log.info("Creating role with name: {} requested by: {}", role.getName(), requester);
        Role createdRole = roleService.saveRole(requester, role);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE)")
    public ResponseEntity<Role> getRoleById(@PathVariable Long roleId) {
        String requester = getRequester();
        log.info("Fetching role with ID: {} requested by: {}", roleId, requester);
        Role role = roleService.findRoleById(roleId);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_ROLE)")
    public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestBody Role roleDetails) {
        String requester = getRequester();
        log.info("Updating role with ID: {} requested by: {}", roleId, requester);
        Role updatedRole = roleService.updateRole(requester, roleId, roleDetails);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_ROLE)")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        String requester = getRequester();
        log.info("Deleting role with ID: {} requested by: {}", roleId, requester);
        roleService.deleteRole(requester, roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_PERMISSION) and hasAuthority(T(com.example.campus.util.Permissions).READ_ROLE)")
    public ResponseEntity<List<Permission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        String requester = getRequester();
        log.info("Fetching permissions for role with ID: {} requested by: {}", roleId, requester);
        List<Permission> permissions = roleService.getPermissionsByRoleId(roleId);
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @PostMapping("/{roleId}/permission/{permissionId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_PERMISSION) and hasAuthority(T(com.example.campus.util.Permissions).WRITE_ROLE)")
    public ResponseEntity<Role> addPermissionToRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        String requester = getRequester();
        log.info("Adding permission with ID: {} to role with ID: {} requested by: {}", permissionId, roleId, requester);
        Role role = roleService.addPermissionToRole(requester, roleId, permissionId);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}/permission/{permissionId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_PERMISSION) and hasAuthority(T(com.example.campus.util.Permissions).WRITE_ROLE)")
    public ResponseEntity<Role> removePermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        String requester = getRequester();
        log.info("Removing permission with ID: {} from role with ID: {} requested by: {}", permissionId, roleId, requester);
        Role role = roleService.removePermissionFromRole(requester, roleId, permissionId);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
