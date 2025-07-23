package com.jakubbone.repository_browser.service;

import com.jakubbone.repository_browser.client.ApiClient;
import com.jakubbone.repository_browser.dto.RepoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepoService {
    private final ApiClient client;

    public RepoService(ApiClient client) {
        this.client = client;
    }

    public List<RepoResponse> getRepos(String owner) {
        List<ApiClient.Repo> repos = client.extractRepoForOwner(owner);

        return repos.stream()
                .filter(repo -> !repo.folk())
                .map(repo -> {
                    List<ApiClient.Branch> branches = client.extractBranchForRepo(repo.name(), repo.owner().login());

                    List<RepoResponse.BranchResponse> branchResponses = branches.stream()
                            .map(branch -> new RepoResponse.BranchResponse(
                                    branch.name(),
                                    branch.commit().sha())
                            )
                            .collect(Collectors.toList());

                    return new RepoResponse(
                            repo.name(),
                            new RepoResponse.Owner(repo.owner().login()),
                            branchResponses);
                })
                .collect(Collectors.toList());
    }
}
