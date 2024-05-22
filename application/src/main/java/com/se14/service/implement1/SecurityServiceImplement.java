package com.se14.service.implement1;

import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserRole;
import com.se14.repository.UserRepository;
import com.se14.service.SecurityService;

import java.util.List;
import java.util.Map;

public class SecurityServiceImplement implements SecurityService {

    private final UserRepository userRepository;

    public SecurityServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean hasAccess(User user, Project project, UserRole requiredRole) {
        // Check if the user is a member of the project
        Map<User, List<UserRole>> members = project.getMembers();
        if (!members.containsKey(user)) {
            return false;
        }

        // Check if the user has the required role
        List<UserRole> roles = members.get(user);
        return roles.contains(requiredRole);
    }

    @Override
    public boolean isAuthenticated(User user) {
        // Check if the user exists in the repository
        try {
            User foundUser = userRepository.findById(user.getUserId()).orElse(null);
            return foundUser != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
            //return false;
        }
    }
}
