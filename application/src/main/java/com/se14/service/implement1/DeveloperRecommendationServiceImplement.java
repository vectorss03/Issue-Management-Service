package com.se14.service.implement1;

import com.se14.domain.Issue;
import com.se14.domain.Project;
import com.se14.domain.User;
import com.se14.service.DeveloperRecommendationService;

import java.util.*;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vertexai.palm2.api.VertexAiPaLm2Api;
import org.springframework.ai.vertexai.palm2.VertexAiPaLm2EmbeddingClient;

public class DeveloperRecommendationServiceImplement implements DeveloperRecommendationService {

    private VertexAiPaLm2EmbeddingClient embeddingClient;

    public DeveloperRecommendationServiceImplement() {
        VertexAiPaLm2Api vertexAiApi = new VertexAiPaLm2Api("AIzaSyAGWMK7yH1XmPmeEe4z5PE8S0HKZFBJABU");
        this.embeddingClient = new VertexAiPaLm2EmbeddingClient(vertexAiApi);
    }

    @Override
    public List<User> recommendDeveloper(Project project, Issue newIssue) {
        /*
        List<Issue> issues = project.getIssues();
        List<String> issueTitles = new ArrayList<>();
        for (Issue issue : issues) {
            if (issue.getFixer() != null) {
                issueTitles.add(issue.getTitle());
            }
        }
        */
        List<Issue> issues = project.getIssues().stream().filter(issue -> issue.getFixer() != null).toList();
        List<String> issueTitles = issues.stream().map(Issue::getTitle).toList();

        EmbeddingResponse embeddingResponse = embeddingClient.embedForResponse(issueTitles);
        List<Embedding> issueEmbeddings = embeddingResponse.getResults();

        EmbeddingResponse newIssueEmbeddingResponse = embeddingClient.embedForResponse(Collections.singletonList(newIssue.getTitle()));
        Embedding newIssueEmbedding = newIssueEmbeddingResponse.getResult();

        TreeMap<Double, User> similarityScores = new TreeMap<>();
        for (int i = 0; i < issueEmbeddings.size(); i++) {
            double similarity = calculateCosineSimilarity(issueEmbeddings.get(i).getOutput(), newIssueEmbedding.getOutput());
            User fixer = issues.get(i).getFixer();
            similarityScores.put(similarity, fixer);
        }

        // Retrieve top three developers based on highest similarity scores
        List<User> recommendedDevelopers = new ArrayList<>();
        for (Map.Entry<Double, User> entry : similarityScores.descendingMap().entrySet()) {
            if (recommendedDevelopers.size() < 3 && !recommendedDevelopers.contains(entry.getValue())) {
                recommendedDevelopers.add(entry.getValue());
            }
            if (recommendedDevelopers.size() == 3) {
                break;
            }
        }

        return recommendedDevelopers;
        /*
        Set<User> recommendedDevelopers = new HashSet<>();
        while (recommendedDevelopers.size() < 3 && !similarityScores.isEmpty()) {
            recommendedDevelopers.add(similarityScores.pollLastEntry().getValue());
        }

        return new ArrayList<>(recommendedDevelopers);
        */
    }

    private double calculateCosineSimilarity(List<Double> vectorA, List<Double> vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
