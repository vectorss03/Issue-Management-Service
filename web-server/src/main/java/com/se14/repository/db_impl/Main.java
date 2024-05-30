package com.se14.repository.db_impl;

import com.se14.domain.Project;
import com.se14.domain.User;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBInitializer.DatabaseObjects Databases = DBInitializer.initializeDatabase();

        IssueDB issueDB = Databases.getIssueDB();
        ProjectDB projectDB = Databases.getProjectDB();
        CommentDB commentDB = Databases.getCommentDB();
        UserDB userDB = Databases.getUserDB();


        //모킹으로 새로운 프로젝트 생성하고 DB에 저장 테스트.



        System.out.println("successful");
    }
}
