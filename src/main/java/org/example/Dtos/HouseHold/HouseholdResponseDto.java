package org.example.Dtos.HouseHold;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseholdResponseDto
{
    private Integer householdId;
    private String householdName;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String notes;
}