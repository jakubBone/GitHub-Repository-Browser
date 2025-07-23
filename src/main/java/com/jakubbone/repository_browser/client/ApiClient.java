package com.jakubbone.repository_browser.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ApiClient {
    private RestClient restClient;

    public ApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Repo> extractRepoForOwner(String owner) {
        return restClient.get()
                .uri("users/{owner}/repos", owner)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<Branch> extractBranchForRepo(String name, String owner){
        return restClient.get()
                .uri("/repos/{owner}/{repoName}/branches", owner, name)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public record Repo(String name, RepoOwner owner, List<Branch> branches, boolean folk) {
    }

    public record RepoOwner(String login) {
    }

    public record Branch(String name, Commit commit) {
    }

    public record Commit(String sha) {
    }
}
