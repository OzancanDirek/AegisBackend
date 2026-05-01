package org.example.Dtos.VolunteerDtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResultVolunteerDto
{
    private Integer volunteerId;

    private UUID userId;
    private String name;
    private String surname;
    private String phone;

    private Boolean availabilityStatus;
    private String transportType;
    private Integer maxDistanceKm;

    private String city;
    private String district;
    private String neighborhood;

    private List<String> skills;
}