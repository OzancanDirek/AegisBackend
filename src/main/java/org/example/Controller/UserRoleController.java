package org.example.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Dtos.RoleDto.CreateRoleRequest;
import org.example.Dtos.RoleDto.RoleResult;
import org.example.Dtos.RoleDto.UpdateRoleRequest;
import org.example.Repository.UserRepository;
import org.example.Service.IUserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/UserRole")
@RequiredArgsConstructor
public class UserRoleController
{
    private final IUserRoleService userRoleService;
    private final UserRepository userRepository;

    @PostMapping("/assign")
    public Map<String, String> assignRole(
            @RequestParam UUID userId,
            @RequestParam UUID roleId
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
    public Map<String, Object> getUserRoles(@PathVariable UUID userId)
    {
        return userRoleService.getUserRoles(userId);
    }

    @GetMapping("/users")
    public Map<String, Object> getUserList()
    {
        return Map.of("users", userRepository.findAll());
    }


    @PostMapping("/roles")
    public ResponseEntity<RoleResult> createRole(@Valid @RequestBody CreateRoleRequest request)
    {
        RoleResult result = userRoleService.createRole(request);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    @PutMapping("/roles")
    public ResponseEntity<RoleResult> updateRole(@Valid @RequestBody UpdateRoleRequest request)
    {
        RoleResult result = userRoleService.updateRole(request);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<RoleResult> deleteRole(@PathVariable UUID roleId)
    {
        RoleResult result = userRoleService.deleteRole(roleId);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }
}