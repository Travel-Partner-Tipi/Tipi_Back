package com.app.repository;

import com.app.entitiy.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserid(String email);

    User findByAccess(String access);
}