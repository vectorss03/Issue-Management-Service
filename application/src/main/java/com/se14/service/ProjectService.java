package com.se14.service;

import com.se14.domain.Project;
import com.se14.domain.Member;
import com.se14.domain.User;

public interface ProjectService {
    Project create(String name, String description);
    void addMember(Project project, Member member);
    boolean hasUser(User user);
}
