package com.jakubbone.github_repository_browser.dto.response;

import java.util.List;

public record RepoResponse(String owner, String name, List<BranchResponse> branches) {
}
