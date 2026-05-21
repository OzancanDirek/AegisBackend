package org.example.Dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto
{
    private String accessToken;
    private String refreshToken;
    private String role;
    private String email;
    private String name;
    private String userId;
    private String addressId;
    private String message;
}
