package com.se14.controller.modal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.se14.APIServer;
import com.se14.controller.ViewController;
import com.se14.domain.MainViewPanel;
import com.se14.view.modal.CreateProjectModal;
import lombok.Setter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CreateProjectController {
    private final ViewController viewController;
    @Setter
    private CreateProjectModal view;

    private final HttpClient client;

    public CreateProjectController(ViewController viewController) {
        this.viewController = viewController;
        client = viewController.getClient();
    }

    public void showView() {
        view.setVisible(true);
    }

    public void createProject(String title, String description) {
        try {
            URI uri = new URIBuilder(APIServer.URL + "/projects").build();
            System.out.println("POST: " + uri.toString());
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            ObjectNode node = new ObjectMapper().createObjectNode();
            node.put("title", title);
            node.put("description", description);
            postRequest.setEntity(new StringEntity(node.toString(), ContentType.APPLICATION_JSON));

            HttpResponse response = client.execute(postRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                System.out.println("Project created successfully");
                view.clearField();

                view.dispose();
                viewController.setCurrentPanel(MainViewPanel.PROJECT);
            } else {
                System.out.println("Failed to create project");
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
