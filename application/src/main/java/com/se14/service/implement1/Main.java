package com.se14.service.implement1;


import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vertexai.palm2.api.VertexAiPaLm2Api;
import org.springframework.ai.vertexai.palm2.VertexAiPaLm2EmbeddingClient;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String apiKey = "AIzaSyAGWMK7yH1XmPmeEe4z5PE8S0HKZFBJABU";  // 실제 API 키로 교체 필요

        VertexAiPaLm2Api vertexAiApi = new VertexAiPaLm2Api(apiKey);

        VertexAiPaLm2EmbeddingClient embeddingClient = new VertexAiPaLm2EmbeddingClient(vertexAiApi);

        EmbeddingResponse embeddingResponse = embeddingClient.embedForResponse(Arrays.asList("How do you solve this issue?", "What is the best practice in this case?"));

        if (embeddingResponse != null) {
            System.out.println("Embedding: " + embeddingResponse.getResults());
        } else {
            System.out.println("Failed to retrieve embeddings.");
        }
    }
}
