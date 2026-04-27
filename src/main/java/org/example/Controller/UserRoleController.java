package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Repository.UserRepository;
import org.example.Service.IUserRoleService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/UserRole")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class UserRoleController
{
    private final IUserRoleService userRoleService;
    private final UserRepository userRepository;

    @PostMapping("/assign")
    public Map<String, String> assignRole(
            @RequestParam String userId,
            @RequestParam String roleId
    )
    {
        return userRoleService.assignRoleToUser(userId, roleId);
    }

    @GetMapping("/all")
    public Map<String, Object> getRoles()
    {
        return userRoleService.getAllRoles();
    }

    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserRoles(@PathVariable String userId)
    {
        return userRoleService.getUserRoles(userId);
    }

    @GetMapping("/users")
    public Map<String, Object> getUserList()
    {
        return Map.of("users", userRepository.findAll());
    }
}
