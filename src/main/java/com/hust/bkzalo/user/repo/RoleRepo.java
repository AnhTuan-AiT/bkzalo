package com.hust.bkzalo.user.repo;

import com.hust.bkzalo.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
