package com.se14.dto;

import lombok.Data;

@Data
public class FilterDTO {
    private String keyword;
    private String status;
    private String priority;
    private String assignee;
    private String fixer;
    private String reporter;
}
