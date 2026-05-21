package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.RoleDto.CreateRoleRequest;
import org.example.Dtos.RoleDto.RoleResponse;
import org.example.Dtos.RoleDto.RoleResult;
import org.example.Dtos.RoleDto.UpdateRoleRequest;
import org.example.Model.Role;
import org.example.Model.Users;
import org.example.Repository.RoleRepository;
import org.example.Repository.UserRepository;
import org.example.Repository.VolunteerRepository;
import org.example.Service.IUserRoleService;
import org.example.Service.IAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final IAuditService auditService;

    private String currentUserEmail()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "unknown";
    }

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

        auditService.log(
                currentUserEmail(),
                "UPDATE",
                "USER_ROLE",
                userId.toString(),
                user.getName() + " " + user.getSurname() + " kullanıcısına \"" + role.getRoleName() + "\" rolü atandı"
        );

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
                .stream().map(this::toResponse).collect(Collectors.toList());
        return Map.of("roles", roles);
    }

    @Override
    public RoleResult createRole(CreateRoleRequest request)
    {
        if (roleRepository.existsByRoleName(request.getRoleName()))
            return RoleResult.builder().success(false)
                    .message("Bu rol adı zaten mevcut: " + request.getRoleName()).build();

        Role role = Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .build();

        roleRepository.save(role);

        auditService.log(
                currentUserEmail(),
                "CREATE",
                "ROLE",
                null,
                "\"" + request.getRoleName() + "\" rolü oluşturuldu"
        );

        return RoleResult.builder().success(true)
                .message("Rol başarıyla oluşturuldu: " + request.getRoleName()).build();
    }

    @Override
    public RoleResult updateRole(UpdateRoleRequest request)
    {
        Role role = roleRepository.findById(request.getRoleId()).orElse(null);
        if (role == null)
            return RoleResult.builder().success(false).message("Rol bulunamadı").build();

        if (!role.getRoleName().equals(request.getRoleName())
                && roleRepository.existsByRoleName(request.getRoleName()))
            return RoleResult.builder().success(false)
                    .message("Bu rol adı zaten mevcut: " + request.getRoleName()).build();

        String oldName = role.getRoleName();
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        roleRepository.save(role);

        auditService.log(
                currentUserEmail(),
                "UPDATE",
                "ROLE",
                request.getRoleId().toString(),
                "\"" + oldName + "\" → \"" + request.getRoleName() + "\" olarak güncellendi"
        );

        return RoleResult.builder().success(true).message("Rol başarıyla güncellendi").build();
    }

    @Override
    public RoleResult deleteRole(UUID roleId)
    {
        if (!roleRepository.existsById(roleId))
            return RoleResult.builder().success(false).message("Silinecek rol bulunamadı").build();

        roleRepository.findById(roleId).ifPresent(role ->
                auditService.log(
                        currentUserEmail(),
                        "DELETE",
                        "ROLE",
                        roleId.toString(),
                        "\"" + role.getRoleName() + "\" rolü silindi"
                )
        );

        roleRepository.deleteById(roleId);
        return RoleResult.builder().success(true).message("Rol başarıyla silindi").build();
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