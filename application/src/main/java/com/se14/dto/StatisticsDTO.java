package com.se14.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.se14.domain.IssuePriority;
import com.se14.domain.IssueStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.SortedMap;

@Data
@NoArgsConstructor
public class StatisticsDTO {
    @JsonProperty
    private SortedMap<String, Integer> dailyIssueCount;
    @JsonProperty
    private SortedMap<String, Integer> monthlyIssueCount;
    @JsonProperty
    private Map<IssueStatus, Integer> statusCount;
    @JsonProperty
    private Map<IssuePriority, Integer> priorityCount;

}
