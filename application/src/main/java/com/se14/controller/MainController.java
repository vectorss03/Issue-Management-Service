package com.se14.controller;

import com.se14.APIServer;
import com.se14.domain.MainViewPanel;
import com.se14.view.MainView;
import lombok.Setter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainController {
    private final ViewController viewController;
    @Setter
    private MainView view;

    private final HttpClient client;


    public MainController(ViewController viewController) {
        this.viewController = viewController;

        client = viewController.getClient();
    }

    public void showView() {
        view.setVisible(true);
    }

    public void showLoginView() {
        viewController.showLoginView();
    }

    public void showSignInView() {
        viewController.showSignInView();
    }

    public void logout() {
        try {
            URI uri = new URIBuilder(APIServer.URL + "/logout").build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                viewController.getSession().setCurrentUser(null);
                viewController.setCurrentPanel(MainViewPanel.PROJECT);
            } else {
                System.out.println("logout failed");
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToHome() {
        viewController.getSession().setCurrentProject(null);
        viewController.getSession().setCurrentIssue(null);
        viewController.setCurrentPanel(MainViewPanel.PROJECT);
    }

    public void refresh() {
        viewController.refreshMainView();
    }

    public void showProjectPanel() {
        view.updateHeader(viewController.getSession().getCurrentUser(), viewController.getSession().getUserRoles());
        view.showProjectPanel();
    }

    public void showIssuePanel() {
        view.updateHeader(viewController.getSession().getCurrentUser(), viewController.getSession().getUserRoles());
        view.showIssuePanel();
    }

    public void showIssueDetailPanel() {
        view.showIssueDetailPanel();
    }

    public void showAnalysisPanel() {
        view.showAnalysisPanel();
    }

    public void showAddUserModal() {
        viewController.showAddUserModal();
    }

    public void goToProjectHome() {
        viewController.getSession().setCurrentIssue(null);
        viewController.setCurrentPanel(MainViewPanel.ISSUE);
    }

    public void setAnalysisView() {
        viewController.setCurrentPanel(MainViewPanel.ANALYSIS);
    }
}
