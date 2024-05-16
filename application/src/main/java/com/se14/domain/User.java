package com.se14.domain;

import lombok.Data;

import java.util.Map;

@Data
public class User {
    private long id;
    private String username;
    private String password;
    private Map<Project, UserRole> roles; // can be deleted later.
}
