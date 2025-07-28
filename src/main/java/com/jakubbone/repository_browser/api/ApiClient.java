package com.jakubbone.repository_browser.api;

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

    public List<Repo> getReposForOwner(String owner) {
        return restClient.get()
                .uri("users/{owner}/repos", owner)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<Branch> getBranchesForRepo(String name, String owner){
        return restClient.get()
                .uri("/repos/{owner}/{repoName}/branches", owner, name)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public record Repo(String name, RepoOwner owner, List<Branch> branches, boolean fork) {
    }

    public record RepoOwner(String login) {
    }

    public record Branch(String name, Commit commit) {
    }

    public record Commit(String sha) {
    }
}
