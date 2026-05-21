package org.example.Repository;

import org.example.Model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>
{
    List<AuditLog> findAllByOrderByCreatedAtDesc();

    List<AuditLog> findByUserEmailOrderByCreatedAtDesc(String email);

    List<AuditLog> findByEntityTypeOrderByCreatedAtDesc(String entityType);

    List<AuditLog> findByActionOrderByCreatedAtDesc(String action);

    void deleteByCreatedAtBefore(LocalDateTime date);
}
