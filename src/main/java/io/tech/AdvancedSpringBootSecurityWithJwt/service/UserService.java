package io.tech.AdvancedSpringBootSecurityWithJwt.service;

import io.tech.AdvancedSpringBootSecurityWithJwt.models.Roles;
import io.tech.AdvancedSpringBootSecurityWithJwt.models.Users;

import java.util.List;

public interface UserService {
    Users SaveUser(Users users);
    Roles SaveRole(Roles role);
    void AddRoleToUsers(String email , String roleName);
    Users getUserByEMail(String email);
    List<Users> GetUsers();
}
