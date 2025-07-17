package com.jakubbone.github_repository_browser.dto;

import java.util.List;

public class RepositoryResponse {
    private String ownerLogin;
    private String repoName;
    private List<BranchResponse> branches;
}
