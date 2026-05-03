package org.example.Dtos.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Model.AidRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAidRequestDto
{
    private Integer requestId;
    private AidRequest.RequestStatus status;
    private Integer urgencyLevel;
    private String description;
}