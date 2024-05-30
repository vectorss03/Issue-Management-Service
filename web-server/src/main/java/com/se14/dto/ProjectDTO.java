package com.se14.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.se14.domain.Project;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectDTO {
    @JsonProperty
    private final Integer projectId;
    @JsonProperty
    private final String title;
    @JsonProperty
    private final String description;

    public ProjectDTO(Project project) {
        this.projectId = project.getProjectId();
        this.title = project.getProjectTitle();
        this.description = project.getProjectDescription();
    }
}
