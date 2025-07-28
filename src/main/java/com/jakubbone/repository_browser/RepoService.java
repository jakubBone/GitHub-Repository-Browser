package com.jakubbone.repository_browser;

import com.jakubbone.repository_browser.api.ApiClient;
import com.jakubbone.repository_browser.api.ApiClient.Repo;
import com.jakubbone.repository_browser.api.ApiClient.Branch;
import com.jakubbone.repository_browser.dto.RepoResponse;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class RepoService {
    private final ApiClient client;

    public RepoService(ApiClient client) {
        this.client = client;
    }
    public List<RepoResponse> getRepos(String owner) {

        List<Repo> repos = client.getRepoForOwner(owner);

        return repos.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    List<Branch> branches = client.getBranchForRepo(repo.name(), repo.owner().login());

                    List<RepoResponse.BranchResponse> branchResponses = branches.stream()
                            .map(branch -> new RepoResponse.BranchResponse(
                                    branch.name(),
                                    branch.commit().sha())
                            )
                            .collect(toList());

                    return new RepoResponse(
                            repo.name(),
                            new RepoResponse.Owner(repo.owner().login()),
                            branchResponses);
                })
                .collect(toList());
    }
}
