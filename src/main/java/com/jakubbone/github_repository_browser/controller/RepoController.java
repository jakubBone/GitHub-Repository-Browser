package com.jakubbone.github_repository_browser.controller;

import com.jakubbone.github_repository_browser.dto.RepositoryResponse;
import com.jakubbone.github_repository_browser.service.RepositoryService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("github/")
public class RepositoryController {
    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public List<RepositoryResponse> getRepos(@PathVariable String ownerLogin){
        repositoryService.findAndReturnRepos(ownerLogin);
    }
}
