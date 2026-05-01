package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.LoginDto;
import org.example.Security.JwtUtil;
import org.example.Service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController
{

    private final IUserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto loginDto)
    {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody org.example.Dtos.RegisterDto registerDto)
    {
        return ResponseEntity.ok(userService.register(registerDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> body)
    {
        String refreshToken = body.get("refreshToken");

        if (refreshToken == null || !jwtUtil.isTokenValid(refreshToken))
        {
            return ResponseEntity.status(401).body(Map.of("message", "Geçersiz veya süresi dolmuş refresh token"));
        }

        String email = jwtUtil.extractEmail(refreshToken);
        String role = userService.getRoleByEmail(email);

        String newAccessToken = jwtUtil.generateAccessToken(email, role);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}