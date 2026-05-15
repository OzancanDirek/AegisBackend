package org.example.Dtos.RoleDto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse
{

    private UUID roleId;
    private String roleName;
    private String description;
}