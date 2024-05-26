package com.Server.Web_Server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IssueController {

    @GetMapping("/home/showIssue/listIssue")
    public String listIssuePage(){

        return "/issueTemplate/listIssue";
    }

    @GetMapping("/home/showIssue/reportIssue")
    public String reportIssuePage(){

        return "/issueTemplate/reportIssue";
    }

    @GetMapping("/home/showIssue/selectIssue")
    public String selectIssuePage(){

        return "/issueTemplate/selectIssue";
    }

}
