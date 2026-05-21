package org.example.Service;

import org.example.Dtos.UserDto.UserResponseDto;
import org.example.Model.Users;

import java.util.List;

public interface IAdminService
{
    List<UserResponseDto> adminUserList(String email);
}