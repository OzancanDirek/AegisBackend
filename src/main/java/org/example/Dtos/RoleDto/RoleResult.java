package org.example.Dtos.RoleDto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResult
{
    private boolean success;
    private String message;
}
