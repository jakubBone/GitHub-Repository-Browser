package com.jakubbone.repository_browser.controller;

import com.jakubbone.repository_browser.dto.response.RepoResponse;
import com.jakubbone.repository_browser.service.RepoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repositories")
public class RepoController {
    private final RepoService service;

    public RepoController(RepoService service) {
        this.service = service;
    }

    @GetMapping("/{owner}")
    public ResponseEntity<List<RepoResponse>> getRepos(@PathVariable String owner){
        List<RepoResponse> repos = service.getRepos(owner);
        return ResponseEntity.ok(repos);
    }
}
