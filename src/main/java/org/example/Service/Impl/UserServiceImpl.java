package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.LoginDto;
import org.example.Dtos.RegisterDto;
import org.example.Model.Adresses;
import org.example.Model.Users;
import org.example.Repository.AddressRepository;
import org.example.Repository.UserRepository;
import org.example.Service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements IUserService
{
    private final UserRepository userRepository;
    private final AddressRepository adressRepository;

    @Override
    public Map<String, String> login(@RequestBody LoginDto loginDto)
    {
        try
        {
            Users user = userRepository.findByEmail(loginDto.email)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            if (!user.getPasswordHash().equals(loginDto.password))
            {
                throw new RuntimeException("Şifre yanlış");
            }

            String role = user.getRoles().stream()
                    .findFirst()
                    .map(r -> r.getRoleName())
                    .orElse("User");

            return Map.of(
                    "message", "Giriş başarılı",
                    "email", user.getEmail(),
                    "role", role
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

            Users user = new Users();
            user.setEmail(registerDto.getEmail());
            user.setName(registerDto.getName());
            user.setSurname(registerDto.getSurname());
            user.setPhone(registerDto.getPhone());
            user.setPasswordHash(registerDto.getPassword());
            user.setStatus(Users.UserStatus.ACTIVE);
            user.setCreatedAt(LocalDateTime.now());

            user.setAddress(address);

            userRepository.save(user);
            return Map.of("message", "Kayıt başarılı");
        }
        catch (Exception e)
        {
            return Map.of("message", "Bir hata oluştu: " + e.getMessage());
        }
    }


}