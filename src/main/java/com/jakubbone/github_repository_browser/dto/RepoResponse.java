package com.jakubbone.github_repository_browser.dto;

import java.util.List;

public record RepoResponse(String owner, String repoName, List<BranchResponse> branches) {
}
