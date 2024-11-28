package com.example.campus.controller;

import com.example.campus.entity.Permission;
import com.example.campus.service.PermissionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@Slf4j
public class PermissionController extends BaseController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_PERMISSION)")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        String requester = getRequester();
        log.info("Fetching all permissions requested by: {}", requester);
        List<Permission> permissions = permissionService.findAllPermissions();
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_PERMISSION)")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) {
        String requester = getRequester();
        log.info("Creating permission with name: {} requested by: {}", permission.getName(), requester);
        Permission createdPermission = permissionService.savePermission(requester, permission);
        return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
    }

    @GetMapping("/{permissionId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).READ_PERMISSION)")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long permissionId) {
        String requester = getRequester();
        log.info("Fetching permission with ID: {} requested by: {}", permissionId, requester);
        Permission permission = permissionService.findPermissionById(permissionId);
        return new ResponseEntity<>(permission, HttpStatus.OK);
    }

    @PutMapping("/{permissionId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_PERMISSION)")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long permissionId, @RequestBody Permission permissionDetails) {
        String requester = getRequester();
        log.info("Updating permission with ID: {} requested by: {}", permissionId, requester);
        Permission updatedPermission = permissionService.updatePermission(requester, permissionId, permissionDetails);
        return new ResponseEntity<>(updatedPermission, HttpStatus.OK);
    }

    @DeleteMapping("/{permissionId}")
    @PreAuthorize("hasAuthority(T(com.example.campus.util.Permissions).WRITE_PERMISSION)")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId) {
        String requester = getRequester();
        log.info("Deleting permission with ID: {} requested by: {}", permissionId, requester);
        permissionService.deletePermission(requester, permissionId);
        return ResponseEntity.noContent().build();
    }
}
