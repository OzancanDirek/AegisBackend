package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Model.Users;
import org.example.Repository.UserRepository;
import org.example.Service.IAdminService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminserviceImpl implements IAdminService
{
    private final UserRepository userRepository;

    public List<Users> adminUserList(String email)
    {
        Users currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getRoleName().equals("Admin"));

        if (!isAdmin)
        {
            throw new RuntimeException("Yetkiniz yok");
        }
        return userRepository.findAll();
    }
}
