package org.example.Service;

import java.util.Map;
import java.util.UUID;

public interface IUserRoleService
{
    Map<String, String> assignRoleToUser(UUID userId, UUID roleId);

    Map<String, Object> getAllRoles();

    Map<String, Object> getUserRoles(UUID userId);

}