package org.example.Service;

import java.util.Map;

public interface IUserRoleService
{
    Map<String, String> assignRoleToUser(String userId, String roleId);

    Map<String, Object> getAllRoles();

    Map<String, Object> getUserRoles(String userId);

}
