package org.example.Service.Impl;


import lombok.RequiredArgsConstructor;
import org.example.Dtos.RoleDto.CreateRoleRequest;
import org.example.Dtos.RoleDto.RoleResponse;
import org.example.Dtos.RoleDto.RoleResult;
import org.example.Dtos.RoleDto.UpdateRoleRequest;
import org.example.Dtos.UserDto.UpdateUserRequest;
import org.example.Dtos.UserDto.UserProfileResponse;
import org.example.Model.Role;
import org.example.Model.Users;
import org.example.Repository.RoleRepository;
import org.example.Repository.UserRepository;
import org.example.Repository.VolunteerRepository;
import org.example.Service.IUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements IUserRoleService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VolunteerRepository volunteerRepository;

    @Override
    public Map<String, String> assignRoleToUser(UUID userId, UUID roleId)
    {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User bulunamadi"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role bulunamadi"));

        user.getRoles().clear();
        user.getRoles().add(role);

        userRepository.save(user);

        return Map.of("message", "Rol atandı");
    }


    @Override
    public Map<String, Object> getUserRoles(UUID userId)
    {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User bulunamadı"));

        return Map.of("roles", user.getRoles());
    }

    @Override
    public Map<String, Object> getAllRoles()
    {
        List<RoleResponse> roles = roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return Map.of("roles", roles);
    }

    @Override
    public RoleResult createRole(CreateRoleRequest createRoleRequest)
    {
        if (roleRepository.existsByRoleName(createRoleRequest.getRoleName()))
        {
            return RoleResult.builder()
                    .success(false)
                    .message("Bu rol adı zaten mevcut: " + createRoleRequest.getRoleName())
                    .build();
        }

        Role role = Role.builder()
                .roleName(createRoleRequest.getRoleName())
                .description(createRoleRequest.getDescription())
                .build();

        roleRepository.save(role);

        return RoleResult.builder()
                .success(true)
                .message("Rol başarıyla oluşturuldu: " + createRoleRequest.getRoleName())
                .build();
    }

    @Override
    public RoleResult updateRole(UpdateRoleRequest request)
    {
        Role role = roleRepository.findById(request.getRoleId()).orElse(null);
        if (role == null)
        {
            return RoleResult.builder()
                    .success(false)
                    .message("Rol bulunamadı")
                    .build();
        }

        if (!role.getRoleName().equals(request.getRoleName())
                && roleRepository.existsByRoleName(request.getRoleName()))
        {
            return RoleResult.builder()
                    .success(false)
                    .message("Bu rol adı zaten mevcut: " + request.getRoleName())
                    .build();
        }

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        roleRepository.save(role);
        return RoleResult.builder()
                .success(true)
                .message("Rol başarıyla güncellendi")
                .build();
    }

    @Override
    public RoleResult deleteRole(UUID roleId)
    {
        if (roleRepository.existsById(roleId))
        {
            roleRepository.deleteById(roleId);
            return RoleResult.builder()
                    .success(true)
                    .message("Rol başarıyla silindi")
                    .build();
        }
        else
        {
            return RoleResult.builder()
                    .success(false)
                    .message("Silinecek rol bulunamadı")
                    .build();
        }

    }

    private RoleResponse toResponse(Role role)
    {
        return RoleResponse.builder()
                .roleId(role.getRoleId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .build();
    }
}


