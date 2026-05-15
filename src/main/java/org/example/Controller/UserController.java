package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.UserDto.UpdateUserRequest;
import org.example.Dtos.UserDto.UserProfileResponse;
import org.example.Model.Users;
import org.example.Repository.UserRepository;
import org.example.Service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class UserController
{
    private final IUserService userService;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication)
    {
        String email = authentication.getName();
        Users user =userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        return ResponseEntity.ok(userService.getProfile(user.getUserId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(Authentication authentication,@RequestBody UpdateUserRequest request)
    {
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        return ResponseEntity.ok(userService.updateProfile(user.getUserId(), request));
    }
}