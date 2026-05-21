package org.example.ScheduleJobs;

import lombok.RequiredArgsConstructor;
import org.example.Repository.AuditLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuditLogCleanupJob
{
    private final AuditLogRepository auditLogRepository;

    @Scheduled(cron = "0 0 2 * * *") // Her gece saat 02:00'de çalışır
    public void cleanOldLogs()
    {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        auditLogRepository.deleteByCreatedAtBefore(cutoff);
        System.out.println("Audit log temizlendi: " + cutoff + " öncesi kayıtlar silindi.");
    }
}