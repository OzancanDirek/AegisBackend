package org.example.Dtos.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAidRequestDto
{
    private Integer householdId;
    private Integer typeId;
    private String description;
    private Integer urgencyLevel; // 1-5
}
