package com.se14.service;

import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserRole;

public interface SecurityService {
    boolean hasAccess(User user, Project project, UserRole requiredRole);
    boolean isAuthenticated(User user);
}
