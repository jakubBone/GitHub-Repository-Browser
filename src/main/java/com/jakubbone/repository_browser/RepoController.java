package com.jakubbone.repository_browser;

import com.jakubbone.repository_browser.dto.ErrorResponse;
import com.jakubbone.repository_browser.dto.RepoResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repositories")
@Log4j2
public class RepoController {
    private final RepoService service;

    public RepoController(RepoService service) {
        this.service = service;
    }

    @GetMapping({"/{owner}", "", "/"})
    public ResponseEntity<?> getRepos(@PathVariable(required = false) String owner){
        if (owner == null || owner.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Owner is missing"));
        }

        List<RepoResponse> repos = service.getRepos(owner);
        return ResponseEntity.ok(repos);
    }
}
