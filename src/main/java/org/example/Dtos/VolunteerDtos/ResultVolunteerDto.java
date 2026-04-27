package org.example.Dtos.VolunteerDtos;

import lombok.Data;

import java.util.List;

@Data
public class ResultVolunteerDto
{
    private Integer volunteerId;

    private String userId;
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