package com.se14.service.implement1;

import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserSession;
import com.se14.domain.Issue;
import com.se14.service.SessionService;

public class SessionServiceImplement implements SessionService {

    private UserSession userSession;

    public SessionServiceImplement() {
        this.userSession = new UserSession();
    }

    @Override
    public UserSession getCurrentSession() {
        return userSession;
    }

    @Override
    public void setCurrentUser(User user) {
        userSession.setCurrentUser(user);
    }

    @Override
    public void setCurrentProject(Project project) {
        userSession.setCurrentProject(project);
    }

    @Override
    public void setCurrentIssue(Issue issue) {
        userSession.setCurrentIssue(issue);
    }
}
