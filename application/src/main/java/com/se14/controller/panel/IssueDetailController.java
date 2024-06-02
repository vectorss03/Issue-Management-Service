package com.se14.controller.panel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.se14.APIServer;
import com.se14.controller.ViewController;
import com.se14.domain.IssueStatus;
import com.se14.domain.UserRole;
import com.se14.dto.CommentDTO;
import com.se14.dto.IssueDTO;
import com.se14.view.panel.IssueDetailPanel;
import lombok.Setter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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

public class IssueDetailController {
    private final ViewController viewController;
    @Setter
    private IssueDetailPanel view;

    private final HttpClient client;

    private IssueDTO issue;

    public IssueDetailController(ViewController viewController) {
        this.viewController = viewController;
        client = viewController.getClient();
    }

    private boolean isProjectLead() {
        List<UserRole> userRoles = viewController.getSession().getUserRoles();
        return userRoles.contains(UserRole.ADMIN) || userRoles.contains(UserRole.PROJECT_LEAD);
    }

    private boolean canChangeState(IssueDTO issue) {
        List<UserRole> userRoles = viewController.getSession().getUserRoles();
        return switch (issue.getStatus()) {
            case NEW, REOPENED -> false;
            case ASSIGNED -> viewController.getSession().getCurrentUser().getUsername().equals(issue.getAssignee());
            case FIXED -> userRoles.contains(UserRole.TESTER) || userRoles.contains(UserRole.ADMIN);
            case RESOLVED, CLOSED -> userRoles.contains(UserRole.PROJECT_LEAD) || userRoles.contains(UserRole.ADMIN);
        };
    }

    public void displayDetailedIssue() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            int issueId = viewController.getSession().getCurrentIssue().getIssueId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/issues/" + issueId).build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                HttpClientResponseHandler<String> handler = new BasicHttpClientResponseHandler();
                String body = handler.handleResponse((ClassicHttpResponse) response);

                ObjectMapper mapper = new ObjectMapper();
                IssueDTO issue = mapper.readValue(body, IssueDTO.class);
                this.issue = issue;

                view.setIssue(issue);
                view.setButtons(isProjectLead() && (issue.getStatus() == IssueStatus.NEW || issue.getStatus() == IssueStatus.REOPENED), canChangeState(issue));
                setComments();
            } else {
                System.out.println("failed to load issues");
                view.setIssue(null);
            }
        } catch (IOException | URISyntaxException | HttpException e) {
            throw new RuntimeException(e);
        }
    }

    private void setComments() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            int issueId = viewController.getSession().getCurrentIssue().getIssueId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/issues/" + issueId + "/comments").build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                HttpClientResponseHandler<String> handler = new BasicHttpClientResponseHandler();
                String body = handler.handleResponse((ClassicHttpResponse) response);

                ObjectMapper mapper = new ObjectMapper();
                List<CommentDTO> comments = mapper.readValue(body, new TypeReference<List<CommentDTO>>(){});

                view.setComments(comments);
            } else {
                System.out.println("failed to load issues");
                view.setComments(new ArrayList<>());
            }
        } catch (IOException | URISyntaxException | HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public void postComment(String content) {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            int issueId = viewController.getSession().getCurrentIssue().getIssueId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/issues/" + issueId + "/comments").build();
            System.out.println("POST: " + uri.toString());
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            ObjectNode node = new ObjectMapper().createObjectNode();
            node.put("content", content);
            postRequest.setEntity(new StringEntity(node.toString(), ContentType.APPLICATION_JSON));

            HttpResponse response = client.execute(postRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                System.out.println("Comment posted");
                view.clearCommentField();
                displayDetailedIssue();
            } else {
                System.out.println("failed to post comment");
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public IssueStatus getNextStatus(IssueStatus currentStatus) {
        return switch (currentStatus) {
            case NEW, REOPENED -> IssueStatus.ASSIGNED;
            case ASSIGNED -> IssueStatus.FIXED;
            case FIXED -> IssueStatus.RESOLVED;
            case RESOLVED -> IssueStatus.CLOSED;
            case CLOSED -> IssueStatus.REOPENED;
        };
    }

    public void changeState() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            int issueId = viewController.getSession().getCurrentIssue().getIssueId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/issues/" + issueId + "/status").build();
            System.out.println("PUT: " + uri.toString());
            HttpPut postRequest = new HttpPut(uri);
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            ObjectNode node = new ObjectMapper().createObjectNode();
            node.put("status", getNextStatus(issue.getStatus()).toString());
            postRequest.setEntity(new StringEntity(node.toString(), ContentType.APPLICATION_JSON));

            HttpResponse response = client.execute(postRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                System.out.println("State Changed");
                viewController.refreshMainView();
            } else {
                System.out.println("failed to change state");
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAssignDeveloperModal() {
        viewController.showAssignDeveloperModal();
    }
}
