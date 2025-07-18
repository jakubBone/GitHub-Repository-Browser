package com.jakubbone.github_repository_browser.service;

import com.jakubbone.github_repository_browser.dto.RepoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RepoService {
    public List<RepoResponse> findRepos(String owner) {
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Non-existent owner");
        }
    return null;
    }
}
