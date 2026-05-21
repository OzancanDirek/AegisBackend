package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Model.AuditLog;
import org.example.Repository.AuditLogRepository;
import org.example.Service.IAuditService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements IAuditService
{
    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(String userEmail, String action, String entityType, String entityId, String detail)
    {
        AuditLog log = AuditLog.builder()
                .userEmail(userEmail)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .detail(detail)
                .build();
        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLog> getAll()
    {
        return auditLogRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<AuditLog> getByUser(String email)
    {
        return auditLogRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }

    @Override
    public List<AuditLog> getByEntityType(String entityType)
    {
        return auditLogRepository.findByEntityTypeOrderByCreatedAtDesc(entityType);
    }

    @Override
    public List<AuditLog> getByAction(String action)
    {
        return auditLogRepository.findByActionOrderByCreatedAtDesc(action);
    }
}