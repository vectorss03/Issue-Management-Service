package com.se14.service.implement1;

import com.se14.domain.User;
import com.se14.repository.UserRepository;
import com.se14.repository.db_impl.DBInitializer;
import com.se14.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;

    public UserServiceImplement(UserRepository userRepository) {
        this.userRepository = DBInitializer.DatabaseObjects.getInstance().getUserDB();
    }

    @Override
    public User authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        if(user == null)
        {
            System.out.println("no user found");
        }else
        if(!user.getPassword().equals(password)) {
            System.out.println("Wrong password");
        }
        return null;
    }

    @Override
    public void addNewUser(String username, String password, String email) {
        User newUser = new User(password);
        newUser.setUsername(username);
        newUser.setEmail(email);
        userRepository.save(newUser);
    }

    @Override
    public List<User> listAllUser() {
        return userRepository.findAll();
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
