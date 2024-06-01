package com.se14.controller;

import com.se14.view.panel.AnalysisPanel;
import lombok.Setter;
import org.apache.hc.client5.http.classic.HttpClient;

public class AnalysisController {
    private final ViewController viewController;
    @Setter
    private AnalysisPanel view;

    private final HttpClient client;

    public AnalysisController(ViewController viewController) {
        this.viewController = viewController;
        client = viewController.getClient();
    }
}
