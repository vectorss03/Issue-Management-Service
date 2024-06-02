package com.se14.controller.modal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.se14.APIServer;
import com.se14.controller.ViewController;
import com.se14.domain.MainViewPanel;
import com.se14.domain.UserRole;
import com.se14.dto.UserDTO;
import com.se14.view.modal.AddUserModal;
import lombok.Setter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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

public class AddUserController {
    private final ViewController viewController;
    @Setter
    private AddUserModal view;

    private final HttpClient client;

    private List<UserDTO> users;

    public AddUserController(ViewController viewController) {
        this.viewController = viewController;
        client = viewController.getClient();
    }

    public void showView() {
        users = getUserJoinList();
        view.setUserPanel(users);
        view.setVisible(true);
    }

    public void searchUsers(String keyword) {
        view.setUserPanel(users.stream().filter(user -> user.getUsername().toLowerCase().contains(keyword.toLowerCase())).toList());
    }

    public List<UserDTO> getUserJoinList() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/users/join").build();
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
                System.out.println("Failed to load user join list");
                return new ArrayList<>();
            }
        } catch (IOException | URISyntaxException | HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(String username, List<UserRole> userRoles) {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/users").build();
            System.out.println("POST: " + uri.toString());
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            ObjectNode node = new ObjectMapper().createObjectNode();
            node.put("username", username);
            ArrayNode rolesNode = node.putArray("userRoles");
            for (UserRole userRole : userRoles) {
                rolesNode.add(userRole.toString());
            }
            postRequest.setEntity(new StringEntity(node.toString(), ContentType.APPLICATION_JSON));

            HttpResponse response = client.execute(postRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                System.out.println("User added");
                view.clearField();

                view.dispose();
                viewController.refreshMainView();
            } else {
                System.out.println("Failed to add user");
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
