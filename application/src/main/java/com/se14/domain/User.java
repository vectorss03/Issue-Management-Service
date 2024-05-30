package com.se14.domain;

import java.util.List;

public class User {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    //private List<Project> projects;

    // Default constructor
    public User() {
        this.userId =0;
    }
    public User(String password) {
        this.userId = 0;
        this.password = password;
    }
    @Override
    public String toString()
    {
        return this.username;
    }

    // Parameterized constructor
    public User(Integer userId,String username, String password, String email) { //projects에 유저가 참여하고 있는 모든 project 만들어서 줘야합니다. -> 필요없을 것 같아 제외했습니다.
        this.userId =userId;
        this.username = username;
        this.password = password;
        this.email = email;
        //this.projects = projects;
    }

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }
    //비밀번호 setter는 없습니다. 불안정적일 것 같네요. 
    // Getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for projects
    /*
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
*/
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
