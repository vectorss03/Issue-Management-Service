package com.se14.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.se14.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectDTO {
    @JsonProperty
    private Integer projectId;
    @JsonProperty
    private String title;
    @JsonProperty
    private String description;

    public ProjectDTO(Project project) {
        this.projectId = project.getProjectId();
        this.title = project.getProjectTitle();
        this.description = project.getProjectDescription();
    }
}
