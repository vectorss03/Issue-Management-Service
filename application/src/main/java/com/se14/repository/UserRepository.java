package com.se14.repository;

import com.se14.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(long id);
    User findByUsername(String username);
    List<User> findAll();
}
