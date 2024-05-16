package com.se14.service;

import com.se14.domain.User;

public interface UserService {
    User join(User user);
    User authenticateUser(String username, String password);
}
