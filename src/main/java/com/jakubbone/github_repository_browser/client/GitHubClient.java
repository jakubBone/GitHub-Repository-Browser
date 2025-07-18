package com.jakubbone.github_repository_browser.client;

import com.jakubbone.github_repository_browser.dto.GithubApiBranch;
import com.jakubbone.github_repository_browser.dto.GithubRepoApi;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GitHubClient {
    private RestClient restClient;

    public List<GithubRepoApi> extractRepoForOwner(String owner) {
        return restClient.get()
                .uri("users/{owner}/repos", owner)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<GithubApiBranch> extractBranchForRepo(String owner, String repoName){
        return restClient.get()
                .uri("/repos/{owner}/{repoName}/branches", owner, repoName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
