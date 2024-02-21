package com.app.repository;
import com.app.entitiy.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(String userId);
}
