package com.hust.bkzalo;

import com.hust.bkzalo.user.entity.Role;
import com.hust.bkzalo.user.entity.User;
import com.hust.bkzalo.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class BkzaloApplication {

    public static void main(String[] args) {
        SpringApplication.run(BkzaloApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.saveRole(new Role("ROLE_USER"));
//            userService.saveRole(new Role("ROLE_ADMIN"));
//            userService.saveRole(new Role("ROLE_MANAGER"));
//
//            userService.saveUser(new User("Anh Tuan", "tuanla", "123456", new ArrayList<>()));
//
//            userService.addRoleToUser("tuanla", "ROLE_USER");
//            userService.addRoleToUser("tuanla", "ROLE_ADMIN");
//        };
//    }
}
