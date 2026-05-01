package org.example.Dtos.VolunteerDtos;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVolunteerDto
{
    private UUID userId;
    private Boolean availabilityStatus;
    private String transportType;
    private Integer maxDistanceKm;
    private Integer addressId;
    private Set<Integer> skillIds;
}