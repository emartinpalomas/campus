package com.example.campus.service;

import com.example.campus.entity.Role;
import com.example.campus.exception.RoleNotFoundException;
import com.example.campus.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
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
}
