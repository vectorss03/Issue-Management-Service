package com.se14.service;
import com.se14.domain.*;

import java.util.List;

public interface DeveloperRecommendationService {
    List<User> recommendDeveloper(Project project,Issue issue);
}
