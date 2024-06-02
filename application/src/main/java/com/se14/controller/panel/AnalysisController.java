package com.se14.controller.panel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se14.APIServer;
import com.se14.controller.ViewController;
import com.se14.dto.StatisticsDTO;
import com.se14.view.panel.AnalysisPanel;
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

public class AnalysisController {
    private final ViewController viewController;
    @Setter
    private AnalysisPanel view;

    private final HttpClient client;

    public AnalysisController(ViewController viewController) {
        this.viewController = viewController;
        client = viewController.getClient();
    }

    private StatisticsDTO getChartData() {
        try {
            int projectId = viewController.getSession().getCurrentProject().getProjectId();
            URI uri = new URIBuilder(APIServer.URL + "/projects/" + projectId + "/statistics").build();
            System.out.println("GET: " + uri.toString());
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpResponse response = client.execute(getRequest);
            if (response.getCode() == HttpStatus.SC_SUCCESS) {
                HttpClientResponseHandler<String> handler = new BasicHttpClientResponseHandler();
                String body = handler.handleResponse((ClassicHttpResponse) response);

                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(body, StatisticsDTO.class);
            } else {
                System.out.println("failed to load projects");
                return new StatisticsDTO();
            }
        } catch (IOException | HttpException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayCharts() {
        StatisticsDTO data = getChartData();
        view.setCharts(data);
    }
}
