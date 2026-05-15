package org.example.Dtos.RoleDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest
{

    @NotNull(message = "Rol ID boş olamaz")
    private UUID roleId;

    @NotBlank(message = "Rol adı boş olamaz")
    @Size(max = 50, message = "Rol adı en fazla 50 karakter olabilir")
    private String roleName;

    private String description;
}