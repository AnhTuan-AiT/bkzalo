package com.hust.bkzalo.service;

import com.hust.bkzalo.entity.Account;
import com.hust.bkzalo.entity.Role;
import com.hust.bkzalo.model.BaseOM;
import com.hust.bkzalo.model.SignUpIM;

import java.util.List;

public interface AccountService {
    Account saveUser(Account account);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    Account getUser(String username);

    List<Account> getUsers();

    BaseOM signUp(SignUpIM signUpIM);
}
