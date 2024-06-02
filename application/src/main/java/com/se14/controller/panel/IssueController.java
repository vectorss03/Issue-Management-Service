package com.se14.controller.panel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se14.APIServer;
import com.se14.controller.ViewController;
import com.se14.domain.*;
import com.se14.dto.IssueDTO;
import com.se14.dto.UserDTO;
import com.se14.view.panel.IssuePanel;
import lombok.Setter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class IssueController {
    private final ViewController viewController;
    @Setter
    private IssuePanel view;

    private final HttpClient client;

    public IssueController(ViewController viewController) {
        this.viewController = viewController;

        client = viewController.getClient();
    }

    public void displayProjectIssues() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/issues").build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                HttpClientResponseHandler<String> handler = new BasicHttpClientResponseHandler();
                String body = handler.handleResponse((ClassicHttpResponse) response);

                ObjectMapper mapper = new ObjectMapper();
                List<IssueDTO> issues = mapper.readValue(body, new TypeReference<List<IssueDTO>>() {});

                view.setIssues(issues);
                List<UserRole> userRoles = viewController.getSession().getUserRoles();
                view.setButtons(userRoles.contains(UserRole.ADMIN) || userRoles.contains(UserRole.TESTER));

                view.setFilterUsers(getAllUsers(), viewController.getSession().getCurrentUser().getUsername());
            } else {
                System.out.println("failed to load issues");
                view.setIssues(new ArrayList<>());
            }
        } catch (IOException | URISyntaxException | HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserDTO> getAllUsers() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/users").build();
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
                return new ArrayList<>();
            }
        } catch (IOException | URISyntaxException | HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectIssue(int issueId) {
        Issue issue = new Issue();
        issue.setIssueId(issueId);
        viewController.getSession().setCurrentIssue(new IssueDTO(issue));

        viewController.setCurrentPanel(MainViewPanel.ISSUE_DETAIL);
    }

    public void showReportIssueModal() {
        viewController.showReportIssueModal();
    }

    public void searchIssue(String keyword, IssueStatus status, IssuePriority priority, String assignee, String fixer, String reporter) {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/issues")
                    .addParameter("keyword", keyword)
                    .addParameter("status", status == null ? null : status.toString())
                    .addParameter("priority", priority == null ? null : priority.toString())
                    .addParameter("assignee", assignee)
                    .addParameter("fixer", fixer)
                    .addParameter("reporter", reporter)
                    .build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                HttpClientResponseHandler<String> handler = new BasicHttpClientResponseHandler();
                String body = handler.handleResponse((ClassicHttpResponse) response);

                ObjectMapper mapper = new ObjectMapper();
                List<IssueDTO> issues = mapper.readValue(body, new TypeReference<List<IssueDTO>>() {});

                view.setIssues(issues);
            } else {
                System.out.println("failed to load issues");
                view.setIssues(new ArrayList<>());
            }
        } catch (IOException | URISyntaxException | HttpException e) {
            throw new RuntimeException(e);
        }
    }
}
