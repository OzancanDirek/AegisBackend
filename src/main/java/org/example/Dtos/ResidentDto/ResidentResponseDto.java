package org.example.Dtos.ResidentDto;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

import org.example.Dtos.SpecialNeedsDto.SpecialNeedsResponseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResidentResponseDto
{
    private Integer residentId;
    private String fullName;
    private LocalDate birthDate;
    private String gender;
    private String identityNo;
    private Set<SpecialNeedsResponseDto> specialNeeds;
}