package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.LoginDto;
import org.example.Dtos.RegisterDto;
import org.example.Model.Adresses;
import org.example.Model.Role;
import org.example.Model.Users;
import org.example.Repository.AddressRepository;
import org.example.Repository.RoleRepository;
import org.example.Repository.UserRepository;
import org.example.Security.JwtUtil;
import org.example.Service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService
{
    private final UserRepository userRepository;
    private final AddressRepository adressRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> login(LoginDto loginDto)
    {
        try
        {
            Users user = userRepository.findByEmail(loginDto.email)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            if (!passwordEncoder.matches(loginDto.password, user.getPasswordHash()))
            {
                throw new RuntimeException("Şifre yanlış");
            }

            String role = user.getRoles().stream()
                    .findFirst()
                    .map(r -> r.getRoleName())
                    .orElse("User");

            String accessToken = jwtUtil.generateAccessToken(user.getEmail(), role);
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            return Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken,
                    "role", role,
                    "email", user.getEmail(),
                    "name", user.getName() != null ? user.getName() : ""
            );
        }
        catch (Exception e)
        {
            return Map.of("message", "Bir hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> register(RegisterDto registerDto)
    {
        try
        {
            if (userRepository.findByEmail(registerDto.getEmail()).isPresent())
            {
                throw new RuntimeException("Bu email zaten kayıtlı");
            }

            Adresses address = new Adresses();
            address.setCity(registerDto.getAddress().city);
            address.setDistrict(registerDto.getAddress().district);
            address.setNeighborhood(registerDto.getAddress().neighborhood);
            address.setStreet(registerDto.getAddress().street);
            adressRepository.save(address);

            Role defaultRole = roleRepository.findByRoleName("User")
                    .orElseThrow(() -> new RuntimeException("User rolü bulunamadı"));

            Users user = new Users();
            user.setEmail(registerDto.getEmail());
            user.setName(registerDto.getName());
            user.setSurname(registerDto.getSurname());
            user.setPhone(registerDto.getPhone());
            user.setPasswordHash(passwordEncoder.encode(registerDto.getPassword()));
            user.setStatus(Users.UserStatus.ACTIVE);
            user.setCreatedAt(LocalDateTime.now());
            user.setAddress(address);
            user.setRoles(new HashSet<>(List.of(defaultRole)));
            userRepository.save(user);

            return Map.of("message", "Kayıt başarılı");
        }
        catch (Exception e)
        {
            return Map.of("message", "Bir hata oluştu: " + e.getMessage());
        }
    }

    public String getRoleByEmail(String email)
    {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return user.getRoles().stream()
                .findFirst()
                .map(Role::getRoleName)
                .orElse("User");
    }
}