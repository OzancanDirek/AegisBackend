package org.example.Service.Impl;


import lombok.RequiredArgsConstructor;
import org.example.Model.Role;
import org.example.Model.Users;
import org.example.Repository.RoleRepository;
import org.example.Repository.UserRepository;
import org.example.Service.IUserRoleService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements IUserRoleService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Map<String, String> assignRoleToUser(String userId, String roleId)
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
    public Map<String, Object> getAllRoles()
    {
        return Map.of("roles", roleRepository.findAll());
    }

    @Override
    public Map<String, Object> getUserRoles(String userId)
    {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User bulunamadı"));

        return Map.of("roles", user.getRoles());
    }
}
