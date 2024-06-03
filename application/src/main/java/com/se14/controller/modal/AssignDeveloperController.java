package com.se14.controller.modal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.se14.APIServer;
import com.se14.controller.ViewController;
import com.se14.dto.UserDTO;
import com.se14.view.modal.AssignDeveloperModal;
import lombok.Setter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class AssignDeveloperController {
    private final ViewController viewController;
    @Setter
    private AssignDeveloperModal view;

    private final HttpClient client;

    private List<UserDTO> users;

    public AssignDeveloperController(ViewController viewController) {
        this.viewController = viewController;
        this.client = viewController.getClient();
    }

    public void showView() {
        users = getAllUsers();
        view.setUserPanel(users);
        view.setVisible(true);

        new Thread(() -> view.setRecommendedUserPanel(getRecommendedUsers())).start();
    }

    public void searchUsers(String keyword) {
        view.setUserPanel(users.stream().filter(user -> user.getUsername().toLowerCase().contains(keyword.toLowerCase())).toList());
    }

    public List<UserDTO> getRecommendedUsers() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            int issueId = viewController.getSession().getCurrentIssue().getIssueId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/issues/" + issueId + "/assignee/recommended").build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                HttpClientResponseHandler<String> handler = new BasicHttpClientResponseHandler();
                String body = handler.handleResponse((ClassicHttpResponse) response);

                ObjectMapper mapper = new ObjectMapper();

                return mapper.readValue(body, new TypeReference<List<UserDTO>>() {});
            } else {
                System.out.println("Failed to load recommended users");
                return new ArrayList<>();
            }
        } catch (IOException | URISyntaxException | HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserDTO> getAllUsers() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/developers").build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                HttpClientResponseHandler<String> handler = new BasicHttpClientResponseHandler();
                String body = handler.handleResponse((ClassicHttpResponse) response);

                ObjectMapper mapper = new ObjectMapper();

                return mapper.readValue(body, new TypeReference<List<UserDTO>>() {});
            } else {
                System.out.println("Failed to load all users");
                return new ArrayList<>();
            }
        } catch (IOException | URISyntaxException | HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public void assignDeveloper(String username) {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            int issueId = viewController.getSession().getCurrentIssue().getIssueId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/issues/" + issueId + "/assignee").build();
            System.out.println("PUT: " + uri.toString());
            HttpPut postRequest = new HttpPut(uri);
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            ObjectNode node = new ObjectMapper().createObjectNode();
            node.put("assignee", username);
            postRequest.setEntity(new StringEntity(node.toString(), ContentType.APPLICATION_JSON));

            HttpResponse response = client.execute(postRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                System.out.println("Developer assigned");
                view.clearField();

                view.dispose();
                viewController.refreshMainView();
            } else {
                System.out.println("failed to assign developer");
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
