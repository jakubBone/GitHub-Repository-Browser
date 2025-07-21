package com.jakubbone.github_repository_browser.dto;

import java.util.List;

public record GithubApiRepo(String name, GithubApiOwner owner, List<GithubApiBranch> branches, boolean folk) {
}
