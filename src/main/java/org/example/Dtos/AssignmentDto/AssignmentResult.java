package org.example.Dtos.AssignmentDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentResult
{

    private boolean success;
    private String message;
    private Integer assignmentId;
}