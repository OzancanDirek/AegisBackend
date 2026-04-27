package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.LoginDto;
import org.example.Dtos.RegisterDto;
import org.example.Service.Impl.UserServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class LoginController
{
    private final UserServiceImpl userService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDto loginDto)
    {
        return userService.login(loginDto);
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterDto registerDto)
    {
        return userService.register(registerDto);
    }
}
