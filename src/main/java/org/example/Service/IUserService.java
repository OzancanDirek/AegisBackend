package org.example.Service;

import org.example.Dtos.LoginDto;
import org.example.Dtos.RegisterDto;

import java.util.Map;

public interface IUserService
{
    Map<String, String> login(LoginDto dto);
    Map<String, String> register(RegisterDto registerDto);
    public String getRoleByEmail(String email);

}
