package org.example.Dtos.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Model.AidRequest;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AidRequestResponseDto
{
    private Integer requestId;
    private Integer householdId;
    private String householdName;
    private Integer typeId;
    private String typeName;
    private String category;
    private String description;
    private Integer urgencyLevel;
    private AidRequest.RequestStatus status;
    private LocalDateTime createdAt;
}
