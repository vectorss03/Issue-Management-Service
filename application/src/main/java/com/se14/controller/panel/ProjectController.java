package com.se14.controller.panel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se14.APIServer;
import com.se14.controller.ViewController;
import com.se14.domain.MainViewPanel;
import com.se14.domain.Project;
import com.se14.domain.UserRole;
import com.se14.dto.ProjectDTO;
import com.se14.view.panel.ProjectPanel;
import lombok.Setter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.net.URIBuilder;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ProjectController {
    private final ViewController viewController;
    @Setter
    private ProjectPanel view;

    private final HttpClient client;

    public boolean isAuthenticated() {
        return viewController.getSession().getCurrentUser() != null;
    }

    public ProjectController(ViewController viewController) {
        this.viewController = viewController;

        client = viewController.getClient();
    }

    public void displayUserProjects() {
        try {
            URI uri = new URIBuilder(APIServer.URL + "/projects").build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                HttpClientResponseHandler<String> handler = new BasicHttpClientResponseHandler();
                String body = handler.handleResponse((ClassicHttpResponse) response);

                ObjectMapper mapper = new ObjectMapper();
                List<ProjectDTO> projects = mapper.readValue(body, new TypeReference<List<ProjectDTO>>() {});

                view.setProjects(projects);
            } else {
                System.out.println("failed to load projects");
                view.setProjects(new ArrayList<>());
            }
        } catch (HttpException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            view.showServerErrorMessage();
        }
    }

    public void selectProject(Integer projectId) {
        Project project = new Project();
        project.setProjectId(projectId);
        viewController.getSession().setCurrentProject(new ProjectDTO(project));

        getUserRoles();

        viewController.setCurrentPanel(MainViewPanel.ISSUE);
    }

    public void getUserRoles() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/roles").build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                HttpClientResponseHandler<String> handler = new BasicHttpClientResponseHandler();
                String body = handler.handleResponse((ClassicHttpResponse) response);

                ObjectMapper mapper = new ObjectMapper();
                List<UserRole> userRoles = mapper.readValue(body, new TypeReference<List<UserRole>>() {});

                viewController.getSession().setUserRoles(userRoles);
                System.out.println("User Roles: " + userRoles);
            } else {
                System.out.println("failed to load user roles");
                viewController.getSession().setUserRoles(new ArrayList<>());
            }
        } catch (IOException | URISyntaxException | HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public void showCreateProjectModal() {
        viewController.showCreateProjectModal();
    }
}
