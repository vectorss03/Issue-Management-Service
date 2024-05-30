package com.se14.service;

import com.se14.domain.*;

import java.util.*;

public interface ProjectService {
    List<Project> listProject();
    Project createProject(User creator,String name, String description);
    void addMemberToProject(Project project,User user, UserRole role);
    HashMap<String, Object> getStatistic(Project project);
    List<User> listUser(Project project, UserRole role);
    boolean hasUser(Project project,User user);
    Project findProjectById(Integer id);
    List<Project> findProjectByUser(User user);  // 현석님 요청. 새로운 메서드 추가
}
