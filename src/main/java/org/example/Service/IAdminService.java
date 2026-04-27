package org.example.Service;

import org.example.Model.Users;

import java.util.List;

public interface IAdminService
{
    List<Users> adminUserList(String email);
}