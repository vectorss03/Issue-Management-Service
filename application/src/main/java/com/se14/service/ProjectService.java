package com.se14.service;

import com.se14.domain.*;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface ProjectService {
    List<Project> listProject();
    Project createProject(User creator,String name, String description);
    void addMemberToProject(Project project,User user, UserRole role);
    Map<Calendar,List<Issue>> getStatistic(Project project);
    List<User> listUser(Project project, UserRole role);
    boolean hasUser(Project project,User user);
    Project findProjectById(long id);
}
