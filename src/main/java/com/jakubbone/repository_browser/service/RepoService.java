package com.jakubbone.repository_browser.service;

import com.jakubbone.repository_browser.client.ApiClient;
import com.jakubbone.repository_browser.dto.response.BranchResponse;
import com.jakubbone.repository_browser.dto.ApiBranch;
import com.jakubbone.repository_browser.dto.Repo;
import com.jakubbone.repository_browser.dto.response.RepoResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepoService {
    private final ApiClient client;

    public RepoService(ApiClient client) {
        this.client = client;
    }

    public List<RepoResponse> getRepos(String owner) {
        List<RepoResponse> repoResponses = new ArrayList<>();

        List<Repo> repos = client.extractRepoForOwner(owner);

        for(Repo repo: repos){
            if (repo.folk()) {
                continue;
            }

        List<ApiBranch> apiBranches = client.extractBranchForRepo(repo.name(), repo.owner().login());

        List<BranchResponse> branchResponses = new ArrayList<>();

        for(ApiBranch apiBranch: apiBranches) {
           branchResponses.add(new BranchResponse(apiBranch.name(), apiBranch.commit().sha()));
        }

        repoResponses.add(new RepoResponse(
                repo.name(),
                repo.owner(),
                branchResponses));

        }
        return repoResponses;
    }
}
