package com.se14.repository.fake;

import com.se14.domain.User;
import com.se14.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryFake implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    public UserRepositoryFake() {
        for (long i = 1; i <= 9; i++) {
            User user = new User("password"+i);
            user.setUserId((int) i);
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@example.com");
            users.put(i, user);
        }
    }

    @Override
    public User save(User user) {
        if(user.getUserId()==null) {
            long newId = users.keySet().stream().mapToLong(Long::longValue).max().orElse(0) + 1;
            user.setUserId((int) newId);
        }
        users.put(user.getUserId().longValue(), user);
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return (List<User>) users.values();
    }
}
