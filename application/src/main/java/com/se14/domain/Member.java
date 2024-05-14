package com.se14.domain;

import lombok.Data;

@Data
public class Member {
    private User user;
    private UserRole role;
}
