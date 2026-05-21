package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.UserDto.UserResponseDto;
import org.example.Repository.UserRepository;
import org.example.Service.IAdminService;
import org.example.Service.IAuditService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminserviceImpl implements IAdminService
{
    private final UserRepository userRepository;
    private final IAuditService auditService;

    public List<UserResponseDto> adminUserList(String email)
    {
        auditService.log(email, "VIEW", "USER_LIST", null, "Admin kullanıcı listesini görüntüledi");

        return userRepository.findAll()
                .stream()
                .map(u -> UserResponseDto.builder()
                        .userId(u.getUserId())
                        .name(u.getName())
                        .surname(u.getSurname())
                        .email(u.getEmail())
                        .build())
                .toList();
    }
}