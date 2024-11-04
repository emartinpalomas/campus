package com.example.campus.repository;

import com.example.campus.entity.Auditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditableRepository extends JpaRepository<Auditable, Long> {
}
