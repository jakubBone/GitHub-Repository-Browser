package com.jakubbone.repository_browser.dto;

import java.util.List;

public record Repo(String name, RepoOwner owner, List<ApiBranch> branches, boolean folk) {
}
