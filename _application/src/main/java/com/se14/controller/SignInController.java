package com.se14.controller;

import com.se14.APIServer;
import com.se14.domain.User;
import com.se14.view.LoginView;
import com.se14.view.SignInView;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SignInController {
    private final ViewController viewController;
    private final SignInView view;

    private final HttpClient client;

    public SignInController(ViewController viewController, SignInView view) {
        this.viewController = viewController;
        this.view = view;

        client = viewController.getClient();
    }

    public void attemptSignIn(String username, String password, String email){
        try {
            URI uri = new URIBuilder(APIServer.URL + "/account")
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("email", email)
                    .build();
            System.out.println(uri.toString());

            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");


            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                System.out.println("Successfully signed in");

                view.showSuccessMessage();
            } else {
                view.showErrorMessage(String.valueOf(response.getCode()));
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}
