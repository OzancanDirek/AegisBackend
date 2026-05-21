package org.example.Service;

import org.example.Dtos.LoginDto;
import org.example.Dtos.LoginResponseDto;
import org.example.Dtos.RegisterDto;
import org.example.Dtos.UserDto.UpdateUserRequest;
import org.example.Dtos.UserDto.UserProfileResponse;
import org.example.Dtos.VolunteerDtos.ResultVolunteerDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IUserService
{
    LoginResponseDto login(LoginDto loginDto);

    Map<String, String> register(RegisterDto registerDto);

    String getRoleByEmail(String email);

    UserProfileResponse getProfile(UUID userId);

    UserProfileResponse updateProfile(UUID userId, UpdateUserRequest request);

}
