package org.example.Dtos.AssignmentDto;

import lombok.*;
import org.example.Model.enums.AssignmentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AidAssignmentResponse
{

    private Integer assignmentId;

    // Talep bilgisi
    private Integer requestId;
    private String requestType;     // AidRequest → AidRequestType → typeName
    private String requestStatus;   // AidRequest'in kendi durumu
    private String householdName;   // hangi haneden geldiği

    // Atanan taraf
    private Integer volunteerId;
    private String volunteerName;

    private Integer teamId;
    private String teamName;

    private AssignmentStatus status;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
    private String notes;
    private LocalDateTime deadline;
}