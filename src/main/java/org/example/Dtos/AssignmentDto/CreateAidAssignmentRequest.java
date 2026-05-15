package org.example.Dtos.AssignmentDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAidAssignmentRequest
{
    @NotNull(message = "Talep ID boş olamaz")
    private Integer requestId;

    private Integer volunteerId;

    private Integer teamId;

    private String notes;
}