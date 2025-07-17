package com.jakubbone.github_repository_browser.dto;

import java.util.List;

public class RepoResponse {
    private String owner;
    private String repoName;
    private List<BranchResponse> branches;
}
