package com.jakubbone.github_repository_browser.controller;

import com.jakubbone.github_repository_browser.dto.RepoResponse;
import com.jakubbone.github_repository_browser.service.RepoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("github/{owner}/repos")
public class RepoController {
    private final RepoService repoService;

    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping
    public List<RepoResponse> getRepos(@PathVariable String owner){
        List<RepoResponse> repos = repoService.findAndReturnRepos(owner);
        return repos;
    }
}
