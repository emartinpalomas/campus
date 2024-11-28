package com.example.campus.service;

import com.example.campus.entity.Auditable;
import com.example.campus.repository.AuditableRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditableService {
    private final AuditableRepository auditableRepository;

    public AuditableService(AuditableRepository auditableRepository) {
        this.auditableRepository = auditableRepository;
    }

    public void updateCreatedBy(Object target, String username) {
        if (target instanceof Auditable auditable) {
            auditable.setCreatedBy(username);
            auditable.setUpdatedBy(username);
            auditableRepository.save(auditable);
        }
    }

    public void updateUpdatedBy(Object target, String username) {
        if (target instanceof Auditable auditable) {
            auditable.setUpdatedBy(username);
            auditableRepository.save(auditable);
        }
    }
}
