package org.example.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.Dtos.AdressDto.AdressDto;

@Data
public class RegisterDto
{
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Email
    @NotBlank
    private String email;

    private String phone;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 character")
    private String password;

    private AdressDto address;
}
