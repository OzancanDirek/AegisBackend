package org.example.Dtos.VolunteerDtos;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVolunteerDto
{
    private String userId;
    private Boolean availabilityStatus;
    private String transportType;
    private Integer maxDistanceKm;
    private Integer addressId;
    private Set<Integer> skillIds;
}