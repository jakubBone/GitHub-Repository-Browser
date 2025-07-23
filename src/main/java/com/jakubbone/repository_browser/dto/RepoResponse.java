package com.jakubbone.repository_browser.dto;

import java.util.List;

public record RepoResponse(String name, Owner owner, List<BranchResponse> branches) {
    public record BranchResponse(String name, String lastCommitSha) {
    }

    public record Owner(String login) {
    }
}
