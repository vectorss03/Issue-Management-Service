package com.Server.Web_Server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    private List<User> users = new ArrayList<>();
    //Repository에서 받아오기

    @GetMapping("/login")
    public ResponseEntity<Void> login(@RequestParam Map<String, String> params, HttpSession session) {
        String username = params.get("username");
        String password = params.get("password");

        boolean userExists = users.stream().anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));

        if (userExists) {
            session.setAttribute("username", username);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<Void> createAccount(@RequestParam Map<String, String> params) {
        int newId = users.isEmpty() ? 1 : users.get(users.size() - 1).getUserId() + 1;
        User newUser = new User();
        newUser.setUserId(newId);
        newUser.setUsername(params.get("username"));
        newUser.setPassword(params.get("password"));
        newUser.setEmail(params.get("email"));
        newUser.setProjects(new ArrayList<>());

        users.add(newUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
