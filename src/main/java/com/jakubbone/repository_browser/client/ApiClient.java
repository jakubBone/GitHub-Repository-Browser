package com.jakubbone.repository_browser.client;

import com.jakubbone.repository_browser.dto.ApiBranch;
import com.jakubbone.repository_browser.dto.Repo;
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

    public List<ApiBranch> extractBranchForRepo(String name, String owner){
        return restClient.get()
                .uri("/repos/{owner}/{repoName}/branches", owner, name)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
