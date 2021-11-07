package com.hust.bkzalo.repo;

import com.hust.bkzalo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Account findByPhoneNumber(String phoneNumber);
}
