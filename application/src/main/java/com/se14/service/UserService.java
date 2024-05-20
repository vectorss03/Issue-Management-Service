package com.se14.service;

import com.se14.domain.User;

public interface UserService {
    //User join(User user);
    User authenticateUser(String username, String password);
    void addNewUser(String username, String password, String email);
}
