package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Model.Users;
import org.example.Repository.UserRepository;
import org.example.Service.IAdminService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminserviceImpl implements IAdminService
{
    private final UserRepository userRepository;

    public List<Users> adminUserList(String email)
    {
        Users currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        System.out.println("DEBUG: Kullanıcı Email: " + email);
        System.out.println("DEBUG: Kullanıcı Rolleri: " + currentUser.getRoles().stream().map(r -> r.getRoleName()).toList());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getRoleName().equalsIgnoreCase("Admin"));

        if (!isAdmin)
        {
            throw new RuntimeException("Yetkiniz yok. Rolleriniz: " + currentUser.getRoles());
        }
        return userRepository.findAll();
    }
}
