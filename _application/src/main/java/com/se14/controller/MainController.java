package com.se14.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se14.APIServer;
import com.se14.domain.User;
import com.se14.dto.ProjectDTO;
import com.se14.view.MainView;
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

public class MainController {
    private final ViewController viewController;
    private final MainView view;

    private final HttpClient client;

   public MainController(ViewController viewController, MainView view) {
       this.viewController = viewController;
       this.view = view;

       client = viewController.getClient();
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
           System.out.println(uri.toString());
           HttpGet getRequest = new HttpGet(uri);
           getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

           HttpResponse response = client.execute(getRequest);
           if (response.getCode() == HttpStatus.SC_SUCCESS) {
               viewController.getSession().setCurrentUser(null);
               viewController.showMainView();
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
       view.showPanel("Projects");
   }

   public void refresh() {

   }

   public void displayUserProjects() {
       try {
           URI uri = new URIBuilder(APIServer.URL + "/projects").build();
           System.out.println(uri.toString());
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
       } catch (IOException | URISyntaxException | HttpException e) {
           throw new RuntimeException(e);
       }
   }
}
