package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Model.Users;
import org.example.Service.IAdminService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor

public class AdminController
{
    private final IAdminService adminservice;

    @GetMapping("/adminUserList")
    public List<Users> adminUserList(@RequestParam String email)
    {
        return adminservice.adminUserList(email);
    }
}
