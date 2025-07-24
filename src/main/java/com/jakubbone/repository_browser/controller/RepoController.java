package com.jakubbone.repository_browser.controller;

import com.jakubbone.repository_browser.dto.ErrorResponse;
import com.jakubbone.repository_browser.dto.RepoResponse;
import com.jakubbone.repository_browser.service.RepoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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

    @GetMapping(path = {"", "/"})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleMissingOwner() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "Owner is missing"));
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(HttpClientErrorException.NotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "Owner not found"));
    }

}
