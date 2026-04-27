package org.example.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:5173") // React'ın (Vite) varsayılan portu
public class TestController
{

    @GetMapping("/hello")
    public Map<String, String> sayHello()
    {
        return Map.of("message", "AEGIS Sistemine Hoş Geldiniz! Backend bağlantısı başarılı.");
    }
}
