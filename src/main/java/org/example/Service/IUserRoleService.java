package org.example.Service;

import org.example.Dtos.RoleDto.CreateRoleRequest;
import org.example.Dtos.RoleDto.RoleResult;
import org.example.Dtos.RoleDto.UpdateRoleRequest;


import java.util.Map;
import java.util.UUID;

public interface IUserRoleService
{
    Map<String, String> assignRoleToUser(UUID userId, UUID roleId);

    Map<String, Object> getAllRoles();

    Map<String, Object> getUserRoles(UUID userId);


    RoleResult createRole(CreateRoleRequest request);

    RoleResult updateRole(UpdateRoleRequest request);

    RoleResult deleteRole(UUID roleId);
}