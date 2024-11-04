package com.example.campus.service;

import com.example.campus.entity.Permission;
import com.example.campus.exception.PermissionNotFoundException;
import com.example.campus.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission findPermissionById(Long permissionId) throws PermissionNotFoundException {
        return permissionRepository.findById(permissionId).orElseThrow(() -> new PermissionNotFoundException("Permission not found with id: " + permissionId));
    }

    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);

    }

    public Permission updatePermission(Long permissionId, Permission permissionDetails) throws PermissionNotFoundException {
        Permission permission = findPermissionById(permissionId);
        if (permissionDetails.getName() != null) permission.setName(permissionDetails.getName());
        return savePermission(permission);
    }

    public void deletePermission(Long permissionId) {
        log.info("Deleting permission with id: {}", permissionId);
        permissionRepository.deleteById(permissionId);
    }
}