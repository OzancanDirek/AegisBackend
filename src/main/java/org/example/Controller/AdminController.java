package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.UserDto.UserResponseDto;
import org.example.Service.IAdminService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor

public class AdminController
{
    private final IAdminService adminservice;

    @GetMapping("/adminUserList")
    public List<UserResponseDto> adminUserList(@RequestParam String email)
    {
        return adminservice.adminUserList(email);
    }
}
