package com.example.campus.service;

import com.example.campus.entity.Permission;
import com.example.campus.exception.PermissionNotFoundException;
import com.example.campus.repository.PermissionRepository;
import com.example.campus.util.EntitySaver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PermissionService {

    private final AuditableService auditableService;
    private final PermissionRepository permissionRepository;

    public PermissionService(AuditableService auditableService, PermissionRepository permissionRepository) {
        this.auditableService = auditableService;
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission findPermissionById(Long permissionId) throws PermissionNotFoundException {
        return permissionRepository.findById(permissionId).orElseThrow(() -> new PermissionNotFoundException("Permission not found with id: " + permissionId));
    }

    public Permission savePermission(String requester, Permission permission) {
        return EntitySaver.saveEntity(auditableService, permissionRepository, requester, permission);

    }

    public Permission updatePermission(String requester, Long permissionId, Permission permissionDetails) throws PermissionNotFoundException {
        Permission permission = findPermissionById(permissionId);
        if (permissionDetails.getName() != null) permission.setName(permissionDetails.getName());
        return savePermission(requester, permission);
    }

    public void deletePermission(String requester, Long permissionId) {
        log.info("Deleting permission with id: {} by user: {}", permissionId, requester);
        permissionRepository.deleteById(permissionId);
    }
}