package org.example.Service;

import org.example.Model.AuditLog;

import java.util.List;

public interface IAuditService
{
    void log(String userEmail, String action, String entityType, String entityId, String detail);

    List<AuditLog> getAll();

    List<AuditLog> getByUser(String email);

    List<AuditLog> getByEntityType(String entityType);

    List<AuditLog> getByAction(String action);
}
