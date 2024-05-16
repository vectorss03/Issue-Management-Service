package com.se14.domain;

public interface SecurityService {
    boolean hasAccess(Member member, Project project, UserRole requiredRole);
    boolean isAuthenticated(User user);
}
