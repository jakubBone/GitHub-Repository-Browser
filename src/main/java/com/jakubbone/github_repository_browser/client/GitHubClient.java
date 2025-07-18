package com.jakubbone.github_repository_browser.client;

import com.jakubbone.github_repository_browser.dto.RepoResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GitHubClient {
    private final RestClient restClient;

    public GitHubClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<RepoResponse> extractRepoForOwner(String owner) {
        return restClient.get()
                .uri("users/{owner}/repos", owner)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<RepoResponse> extractBranchForRepo(String owner, String repoName){
        return restClient.get()
                .uri("/repos/{owner}/{repoName}/branches", owner, repoName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
