package com.jakubbone.repository_browser;

import com.jakubbone.repository_browser.api.ApiClient;
import com.jakubbone.repository_browser.api.ApiClient.Repo;
import com.jakubbone.repository_browser.api.ApiClient.Branch;
import com.jakubbone.repository_browser.dto.RepoResponse;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

//import static java.util.stream.Collectors.toList;

@Service
@Log4j2
public class RepoService {
    private final ApiClient client;

    public RepoService(ApiClient client) {
        this.client = client;
    }

    public List<RepoResponse> getRepos(final String owner) {
        log.debug("Getting repositories for owner: {}", owner);
        final var repos = client.getReposForOwner(owner);
        log.debug("Found {} repositories for owner: {}", repos.size(), owner);

        return repos.parallelStream()
                .filter(repo -> !repo.fork())
                .map(this::mapToRepoResponse)
                .toList();
    }

    private RepoResponse mapToRepoResponse(final Repo repo){
        log.debug("Getting branches for repo: {}", repo.name());
        final var branches = client.getBranchesForRepo(repo.name(), repo.owner().login());
        log.debug("Found {} branches for owner: {}", branches.size(), repo.name());

        List<RepoResponse.BranchResponse> branchResponses = branches.stream()
                .map(this::mapToBranchResponse)
                .toList();

        return new RepoResponse(
                repo.name(),
                new RepoResponse.Owner(repo.owner().login()),
                branchResponses);
    }

    private RepoResponse.BranchResponse mapToBranchResponse(final Branch branch){
        return new RepoResponse.BranchResponse(
                        branch.name(),
                        branch.commit().sha());
    }
}
