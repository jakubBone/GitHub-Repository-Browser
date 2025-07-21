package com.jakubbone.github_repository_browser.service;

import com.jakubbone.github_repository_browser.client.GitHubClient;
import com.jakubbone.github_repository_browser.dto.response.BranchResponse;
import com.jakubbone.github_repository_browser.dto.GithubApiBranch;
import com.jakubbone.github_repository_browser.dto.GithubApiRepo;
import com.jakubbone.github_repository_browser.dto.response.RepoResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubRepoService {
    private final GitHubClient client;

    public GitHubRepoService(GitHubClient client) {
        this.client = client;
    }

    public List<RepoResponse> getRepos(String owner) {
        List<RepoResponse> repoResponses = new ArrayList<>(); // wynik pusty

        // All repos for owner (owner, name, branches[])
        List<GithubApiRepo> repos = client.extractRepoForOwner(owner);

        // For each owner's repo
        for(GithubApiRepo repo: repos){
            if (repo.folk()) {
                continue;
            }

        // Current repo branches
        List<GithubApiBranch> apiBranches = client.extractBranchForRepo(repo.owner(), repo.name());

        List<BranchResponse> branchResponses = new ArrayList<>();

        for(GithubApiBranch apiBranch: apiBranches) {
           branchResponses.add(new BranchResponse(apiBranch.name(), apiBranch.lastCommitSha()));
        }

        repoResponses.add(new RepoResponse(
                repo.owner(),
                repo.name(),
                branchResponses));

        }
        return repoResponses;
    }
}
