package com.se14.controller;

import com.se14.domain.UserSession;
import com.se14.view.LoginView;
import com.se14.view.MainView;
import com.se14.view.SignInView;
import lombok.Getter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

public class ViewController {
    private final MainView mainView;
    private final LoginView loginView;
    private final SignInView signInView;

    @Getter
    private final UserSession session;
    @Getter
    private final HttpClient client;

    public ViewController() {
        session = new UserSession();
        client = HttpClientBuilder.create().build();

        mainView = new MainView();
        mainView.setController(new MainController(this, mainView));

        loginView = new LoginView();
        loginView.setController(new LoginController(this, loginView));

        signInView = new SignInView();
        signInView.setController(new SignInController(this, signInView));
    }

    public void run() {
        showMainView();
    }

    public void showMainView() {
        mainView.updateUser(session.getCurrentUser());
        mainView.setVisible(true);
    }

    public void showLoginView() {
        loginView.setVisible(true);
    }

    public void showSignInView() {
        signInView.setVisible(true);
    }
}
