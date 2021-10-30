package com.hust.bkzalo.user.service;

import com.hust.bkzalo.user.entity.Role;
import com.hust.bkzalo.user.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);

    List<User> getUsers();
}
