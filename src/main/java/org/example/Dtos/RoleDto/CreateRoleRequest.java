package org.example.Dtos.RoleDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoleRequest
{
    @NotBlank(message = "Rol adı boş olamaz")
    @Size(max = 50, message = "Rol adı en fazla 50 karakter olabilir")
    private String roleName;

    private String description;
}
