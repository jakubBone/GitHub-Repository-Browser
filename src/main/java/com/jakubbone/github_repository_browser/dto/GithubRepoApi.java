package com.jakubbone.github_repository_browser.dto;

import java.util.List;

public record GithubRepoApi(String owner, String name, List<GithubApiBranch> branches, boolean folk) {
}
