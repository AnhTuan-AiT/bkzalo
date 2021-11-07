package com.hust.bkzalo.service;

import com.hust.bkzalo.entity.Account;
import com.hust.bkzalo.entity.Role;
import com.hust.bkzalo.model.BaseOM;
import com.hust.bkzalo.model.SignUpIM;
import com.hust.bkzalo.model.SignUpOM;
import com.hust.bkzalo.repo.RoleRepo;
import com.hust.bkzalo.utils.BaseHTTP;
import com.hust.bkzalo.utils.Validator;
import com.hust.bkzalo.repo.AccountRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private final AccountRepo accountRepo;

    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = accountRepo.findByPhoneNumber(s);
        if (account == null) {
            throw new UsernameNotFoundException("Phone number not found in the database");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        account.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(account.getPhoneNumber(), account.getPassword(), authorities);
    }

    @Override
    public Account saveUser(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepo.save(account);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        Account account = accountRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        account.getRoles().add(role);
        accountRepo.save(account);
    }

    @Override
    public Account getUser(String username) {
        return accountRepo.findByUsername(username);
    }

    @Override
    public List<Account> getUsers() {
        return accountRepo.findAll();
    }

    @Override
    public BaseOM signUp(SignUpIM signUpIM) {
        // test case 5 -> auto
        SignUpOM response = new SignUpOM();

        String phoneNumber = signUpIM.getPhoneNumber();
        if (!Validator.isValid(phoneNumber)) {
            response.invalidParameter();
            response.setData("Số điện thoại không đúng định dạng");
            return response; // done test case 3.
        }

        String password = signUpIM.getPassword();
        if (password.equals(phoneNumber) || !Validator.isValidPassword(password)) {
            response.invalidParameter();
            response.setData("Mật khẩu không đúng định dạng");
            return response; // done test case 4.
        }

        Account account = accountRepo.findByPhoneNumber(phoneNumber);
        if (account == null) {
            account = new Account();
            account.setPhoneNumber(phoneNumber);
            account.setPassword(passwordEncoder.encode(password));
            account.setDeviceId(signUpIM.getDeviceId());

            accountRepo.save(account);
            response.success();
            return response; // done test case 1.
        } else {
            response.setCode(String.valueOf(BaseHTTP.CODE_9996));
            response.setMessage(BaseHTTP.MESSAGE_9996);
            response.setData(null);
            return response; // done test case 2;
        }
    }


}
