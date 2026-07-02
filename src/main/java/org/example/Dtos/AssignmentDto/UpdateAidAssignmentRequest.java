package org.example.Dtos.AssignmentDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.Model.enums.AssignmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAidAssignmentRequest
{

    @NotNull(message = "Assignment ID boş olamaz")
    private Integer assignmentId;

    private Integer volunteerId;

    private Integer teamId;

    private AssignmentStatus status;

    private String notes;

    private LocalDateTime deadline;
}