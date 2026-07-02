package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.LoginDto;
import org.example.Dtos.LoginResponseDto;
import org.example.Dtos.RegisterDto;
import org.example.Dtos.UserDto.UpdateUserRequest;
import org.example.Dtos.UserDto.UserProfileResponse;
import org.example.Model.Adresses;
import org.example.Model.Role;
import org.example.Model.Users;
import org.example.Repository.AddressRepository;
import org.example.Repository.RoleRepository;
import org.example.Repository.UserRepository;
import org.example.Repository.VolunteerRepository;
import org.example.Security.JwtUtil;
import org.example.Service.IUserService;
import org.example.Service.IAuditService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService
{
    private final UserRepository userRepository;
    private final AddressRepository adressRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final VolunteerRepository volunteerRepository;
    private final IAuditService auditService;

    @Override
    public LoginResponseDto login(LoginDto loginDto)
    {
        try
        {
            Users user = userRepository.findByEmail(loginDto.email)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            if (!passwordEncoder.matches(loginDto.password, user.getPasswordHash()))
            {
                auditService.log(loginDto.email, "LOGIN_FAILED", "AUTH", null, "Hatalı şifre ile giriş denemesi");
                return LoginResponseDto.builder().message("Şifre yanlış").build();
            }

            String role = user.getRoles().stream()
                    .findFirst()
                    .map(Role::getRoleName)
                    .orElse("User");

            String accessToken = jwtUtil.generateAccessToken(user.getEmail(), role);
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            auditService.log(
                    loginDto.email, "LOGIN", "AUTH",
                    user.getUserId().toString(),
                    user.getName() + " " + user.getSurname() + " sisteme giriş yaptı — Rol: " + role
            );

            return LoginResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .role(role)
                    .email(user.getEmail())
                    .name(user.getName() != null ? user.getName() : "")
                    .userId(user.getUserId().toString())
                    .addressId(user.getAddress() != null ? user.getAddress().getAddressId().toString() : "")
                    .build();
        }
        catch (Exception e)
        {
            return LoginResponseDto.builder().message("Bir hata oluştu").build();
        }
    }

    @Override
    public Map<String, String> register(RegisterDto registerDto)
    {
        try
        {
            if (userRepository.findByEmail(registerDto.getEmail()).isPresent())
                throw new RuntimeException("Bu email zaten kayıtlı");

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

            auditService.log(
                    registerDto.getEmail(),
                    "REGISTER",
                    "AUTH",
                    null,
                    registerDto.getName() + " " + registerDto.getSurname() + " sisteme kayıt oldu"
            );

            return Map.of("message", "Kayıt başarılı");
        }
        catch (Exception e)
        {
            return Map.of("message", "Bir hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public UserProfileResponse updateProfile(UUID userId, UpdateUserRequest request)
    {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getSurname() != null) user.setSurname(request.getSurname());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        userRepository.save(user);

        auditService.log(
                user.getEmail(),
                "UPDATE",
                "USER",
                userId.toString(),
                user.getName() + " " + user.getSurname() + " profil bilgilerini güncelledi"
        );

        return getProfile(userId);
    }

    @Override
    public void changePassword(UUID userId, String oldPassword, String newPassword)
    {
       Users user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("Kullanici bulunamadi"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash()))
        {
            throw new RuntimeException("Eski şifre yanlış");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        auditService.log(
                user.getEmail(),
                "CHANGE_PASSWORD",
                "USER",
                userId.toString(),
                "Kullanici sifresi degistirildi"
        );
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

    @Override
    public UserProfileResponse getProfile(UUID userId)
    {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        UserProfileResponse response = UserProfileResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRoles().stream().findFirst().map(Role::getRoleName).orElse("User"))
                .city(user.getAddress() != null ? user.getAddress().getCity() : null)
                .district(user.getAddress() != null ? user.getAddress().getDistrict() : null)
                .build();

        volunteerRepository.findByUser_UserId(userId).ifPresent(volunteer -> {
            response.setVolunteerId(volunteer.getVolunteerId());
            response.setAvailabilityStatus(volunteer.getAvailabilityStatus());
            response.setTransportType(volunteer.getTransportType());
            response.setMaxDistanceKm(volunteer.getMaxDistanceKm());
            response.setSkills(volunteer.getSkills() != null
                    ? volunteer.getSkills().stream()
                    .map(s -> s.getSkillName())
                    .collect(java.util.stream.Collectors.toList())
                    : null);
        });

        return response;
    }
}