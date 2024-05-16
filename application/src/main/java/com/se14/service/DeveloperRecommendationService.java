package com.se14.service;

import com.se14.domain.Issue;
import com.se14.domain.Member;

import java.util.List;

public interface DeveloperRecommendationService {
    List<Member> getRecommendation(Issue issue);
}
