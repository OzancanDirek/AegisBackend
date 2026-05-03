package org.example.Dtos.HouseHold;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHouseholdDto
{
    private String householdName;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String notes;
}