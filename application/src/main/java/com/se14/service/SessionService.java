package com.se14.service;

import com.se14.domain.Issue;
import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.domain.UserSession;

// 새로 추가될 메서드가 존재?
// 아니면 Domain의 UserSession으로 통합해도 괜찮을듯..?
public interface SessionService {
    UserSession getCurrentSession();
    void setCurrentUser(User user);
    void setCurrentProject(Project project);
    void setCurrentIssue(Issue issue);
}
