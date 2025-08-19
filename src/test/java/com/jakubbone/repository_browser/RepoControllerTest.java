package com.jakubbone.repository_browser;

import com.jakubbone.repository_browser.dto.RepoResponse;
import com.jakubbone.repository_browser.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RepoControllerTest {
    private MockMvc mockMvc;
    private RepoService service;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(RepoService.class);
        var controller = new RepoController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturn400WhenOwnerIsOnlyWhitespace() throws Exception {
        mockMvc.perform(get("/api/v1/repositories/{owner}", "   "))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Owner login must be provided"));
    }

    @Test
    void shouldReturn404WhenOwnerNotFound() throws Exception {
        Mockito.when(service.getRepos(eq("ghost-user-xyz")))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatus.NOT_FOUND,
                        "Not Found",
                        HttpHeaders.EMPTY,
                        null,
                        StandardCharsets.UTF_8
                ));

        mockMvc.perform(get("/api/v1/repositories/{owner}", "ghost-user-xyz"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Owner not found"));
    }

    @Test
    void shouldReturn200WithData() throws Exception {
        var branches = List.of(new RepoResponse.BranchResponse("main", "abc123"));
        var repo = new RepoResponse("my-repo", new RepoResponse.Owner("octocat"), branches);

        Mockito.when(service.getRepos(eq("octocat"))).thenReturn(List.of(repo));

        mockMvc.perform(get("/api/v1/repositories/{owner}", "octocat"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("my-repo"))
                .andExpect(jsonPath("$[0].owner.login").value("octocat"))
                .andExpect(jsonPath("$[0].branches[0].name").value("main"))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha").value("abc123"));
    }
}