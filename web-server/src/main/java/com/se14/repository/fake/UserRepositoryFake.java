package com.se14.repository.fake;

import com.se14.domain.User;
import com.se14.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryFake implements UserRepository {

    private final Map<Integer, User> users = new HashMap<>();

    public UserRepositoryFake() {
        for (int i = 1; i <= 9; i++) {
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
            Integer newId = users.keySet().stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0) + 1;
            user.setUserId((int) newId);
        }
        users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Integer id) {
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
        return new ArrayList<>(users.values());
    }
}
