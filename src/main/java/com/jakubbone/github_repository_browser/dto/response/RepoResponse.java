package com.jakubbone.github_repository_browser.dto.response;

import com.jakubbone.github_repository_browser.dto.GithubApiOwner;

import java.util.List;

public record RepoResponse(String name, GithubApiOwner owner, List<BranchResponse> branches) {
}
