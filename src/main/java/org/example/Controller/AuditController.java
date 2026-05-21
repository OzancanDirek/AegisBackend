package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Model.AuditLog;
import org.example.Service.IAuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController
{
    private final IAuditService auditService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAll()
    {
        return ResponseEntity.ok(auditService.getAll());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<AuditLog>> getByUser(@PathVariable String email)
    {
        return ResponseEntity.ok(auditService.getByUser(email));
    }

    @GetMapping("/entity/{entityType}")
    public ResponseEntity<List<AuditLog>> getByEntityType(@PathVariable String entityType)
    {
        return ResponseEntity.ok(auditService.getByEntityType(entityType));
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLog>> getByAction(@PathVariable String action)
    {
        return ResponseEntity.ok(auditService.getByAction(action));
    }
}