package com.example.campus.util;

import com.example.campus.entity.Auditable;
import com.example.campus.service.AuditableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

@Slf4j
public class EntitySaver {

    public static <T extends Auditable> T saveEntity(AuditableService auditableService, JpaRepository<T, Long> repository, String username, T entity) {
        if (entity.getId() == null) {
            log.info("Creating entity: {} by user: {}", entity.getClass(), username);
            auditableService.updateCreatedBy(entity, username);
        } else {
            log.info("Updating entity: {} with id: {} by user: {}", entity.getClass(), entity.getId(), username);
            auditableService.updateUpdatedBy(entity, username);
        }
        return repository.save(entity);
    }
}
