package com.se14.controller;

import com.se14.APIServer;
import com.se14.domain.User;
import com.se14.view.LoginView;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LoginController {
    private final ViewController viewController;
    private final LoginView view;

    private final HttpClient client;

    public LoginController(ViewController viewController, LoginView view) {
        this.viewController = viewController;
        this.view = view;

        client = viewController.getClient();
    }

    public void attemptLogin(String username, String password){
        try {
            URI uri = new URIBuilder(APIServer.URL + "/login")
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .build();
            System.out.println(uri.toString());

            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");


            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                System.out.println("Successfully logged in");

                User user = new User(password);
                user.setUsername(username);
                viewController.getSession().setCurrentUser(user);

                view.dispose();
                viewController.showMainView();
            } else {
                view.showErrorMessage("Invalid username or password");
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}
