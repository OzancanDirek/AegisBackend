package org.example.ScheduleJobs;

import lombok.RequiredArgsConstructor;
import org.example.Model.AidAssignment;
import org.example.Model.enums.AssignmentStatus;
import org.example.Repository.AidAssignmentRepository;
import org.example.Service.IEmailService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeadlineReminderJob
{
    private final AidAssignmentRepository aidAssignmentRepository;
    private final IEmailService emailService;

    public void sendDeadlineReminder()
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);

        List<AidAssignment> upcoming = aidAssignmentRepository
                .findByDeadlineBetweenAndStatusNot(now, tomorrow, AssignmentStatus.COMPLETED);

        for (AidAssignment assignment : upcoming)
        {
            try
            {
                if (assignment.getAssignedVolunteer() != null &&
                        assignment.getAssignedVolunteer().getUser() != null)
                {
                    String email = assignment.getAssignedVolunteer().getUser().getEmail();
                    String name = assignment.getAssignedVolunteer().getUser().getName();
                    String desc = "Görev #" + assignment.getAssignmentId() + " — " + assignment.getNotes();
                    emailService.sendTaskAssignedEmail(email, name, "⚠️ Deadline Yaklaşıyor: " + desc);
                }
            }
            catch (Exception e)
            {
                System.out.println("Deadline maili gönderilemedi: " + e.getMessage());
            }
        }
    }
}
