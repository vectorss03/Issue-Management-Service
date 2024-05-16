package com.se14.domain;

import lombok.Data;

import java.util.List;

@Data
public class Project {
    private long id;
    private String title;
    private String description;
    private List<Member> members;
    private List<Issue> issues;
}
