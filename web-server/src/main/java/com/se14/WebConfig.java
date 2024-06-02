package com.se14;

import com.se14.repository.db_impl.DBInitializer;
import com.se14.service.IssueService;
import com.se14.service.ProjectService;
import com.se14.service.UserService;
import com.se14.service.implement1.IssueServiceImplement;
import com.se14.service.implement1.ProjectServiceImplement;
import com.se14.service.implement1.UserServiceImplement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/static/index.html");
                    }
                });
    }

    @Bean
    public IssueService issueService() {
        return new IssueServiceImplement(DBInitializer.DatabaseObjects.getInstance().getIssueDB(), DBInitializer.DatabaseObjects.getInstance().getProjectDB(), DBInitializer.DatabaseObjects.getInstance().getCommentDB());
    }

    @Bean
    public ProjectService projectService() {
        return new ProjectServiceImplement(DBInitializer.DatabaseObjects.getInstance().getProjectDB(), DBInitializer.DatabaseObjects.getInstance().getUserDB());
    }

    @Bean
    public UserService userService() {
        return new UserServiceImplement(DBInitializer.DatabaseObjects.getInstance().getUserDB());
    }
}
