package com.jakubbone.repository_browser.dto.response;

import com.jakubbone.repository_browser.dto.RepoOwner;

import java.util.List;

public record RepoResponse(String name, RepoOwner owner, List<BranchResponse> branches) {
}
