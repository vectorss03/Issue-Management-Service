package com.Server.Web_Server.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectController {

    @GetMapping("/project/list")
    public String listProjectPage(){


        return "/projectTemplate/listProject";
    }

    @GetMapping("/project/create")
    public String createProjectpage(){

        return "createProject";
    }

    @GetMapping("/home")
    public String homepage(){

        return "projectTemplate/home";
    }

    @GetMapping("/home/showIssues")
    public String showIssuePage(){

        return "projectTemplate/showIssues";
    }

    @GetMapping("/home/showUsers")
    public String showUsersPage(){

        return "projectTemplate/showUsers";
    }

    @GetMapping("/home/showAnalysis")
    public String showAnalysisPage(){

        return "projectTemplate/showAnalysis";
    }


}
