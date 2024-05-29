package com.se14.repository.db_impl;

import com.se14.domain.Project;
import com.se14.domain.User;

public class Main {
    public static void main(String[] args) {
        DBInitializer.DatabaseObjects Databases = DBInitializer.initializeDatabase();

        IssueDB issueDB = Databases.getIssueDB();
        ProjectDB projectDB = Databases.getProjectDB();
        CommentDB commentDB = Databases.getCommentDB();
        UserDB userDB = Databases.getUserDB();


        //모킹으로 새로운 프로젝트 생성하고 DB에 저장 테스트.

        Project newProject1 = new Project();
        newProject1.setProjectTitle("Test_project");
        newProject1.setProjectDescription("Test_project_description");

        projectDB.save(newProject1);

        User newUser1 = new User();

        System.out.println("successful");
    }
}
