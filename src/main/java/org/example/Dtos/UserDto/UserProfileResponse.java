package org.example.Dtos.UserDto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse
{
    private UUID userId;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String role;
    private String city;
    private String district;
    private Integer volunteerId;

    // Gönüllüyse dolu gelir, değilse null
    private Boolean availabilityStatus;
    private String transportType;
    private Integer maxDistanceKm;
    private java.util.List<String> skills;
}