package com.hust.bkzalo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class BkzaloApplication {

    public static void main(String[] args) {
        SpringApplication.run(BkzaloApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    CommandLineRunner run(AccountService userService, PostRepo postRepo) {
//        return args -> {
////            userService.saveRole(new Role("ROLE_USER"));
////            userService.saveRole(new Role("ROLE_ADMIN"));
////            userService.saveRole(new Role("ROLE_MANAGER"));
////
//            Account account = new Account();
//            account.setUsername("Anh Tuan");
//            account.setPassword("123456");
//            account.setPhoneNumber(969826785L);
//            account.setRoles(new ArrayList<>());
//            account.setDeviceId(UUID.randomUUID());
//            account = userService.saveUser(account);
//
//            userService.addRoleToUser("Anh Tuan", "ROLE_USER");
//            userService.addRoleToUser("Anh Tuan", "ROLE_ADMIN");
//
//            Post post = new Post();
//            post.setAccountId(account.getId());
//            post.setContent("Test");
//            postRepo.save(post);
//        };
//    }
}
