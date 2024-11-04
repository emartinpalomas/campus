package com.example.campus.controller;

import com.example.campus.entity.Permission;
import com.example.campus.service.PermissionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@Slf4j
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        log.info("Fetching all permissions");
        List<Permission> permissions = permissionService.findAllPermissions();
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) {
        log.info("Creating permission with name: {}", permission.getName());
        Permission createdPermission = permissionService.savePermission(permission);
        return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long permissionId) {
        log.info("Fetching permission with ID: {}", permissionId);
        Permission permission = permissionService.findPermissionById(permissionId);
        return new ResponseEntity<>(permission, HttpStatus.OK);
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long permissionId, @RequestBody Permission permissionDetails) {
        log.info("Updating permission with ID: {}", permissionId);
        Permission updatedPermission = permissionService.updatePermission(permissionId, permissionDetails);
        return new ResponseEntity<>(updatedPermission, HttpStatus.OK);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId) {
        log.info("Deleting permission with ID: {}", permissionId);
        permissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }
}
