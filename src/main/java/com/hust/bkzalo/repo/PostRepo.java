package com.hust.bkzalo.repo;

import com.hust.bkzalo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepo extends JpaRepository<Post, UUID> {
}
