package com.jakubbone.repository_browser;

import com.jakubbone.repository_browser.api.ApiClient;
import com.jakubbone.repository_browser.api.ApiClient.Branch;
import com.jakubbone.repository_browser.api.ApiClient.Commit;
import com.jakubbone.repository_browser.api.ApiClient.Repo;
import com.jakubbone.repository_browser.api.ApiClient.RepoOwner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RepoServiceTest {
    private ApiClient api;
    private RepoService service;

    @BeforeEach
    void setUp() {
        api = mock(ApiClient.class);
        service = new RepoService(api);
    }

    @Test
    void filtersOutForksAndMapsBranches() {
        var owner = "octo";
        var repo1 = new Repo("r1", new RepoOwner(owner), null, false);
        var repo2 = new Repo("r2-fork", new RepoOwner(owner), null, true);

        when(api.getReposForOwner(owner)).thenReturn(List.of(repo1, repo2));
        when(api.getBranchesForRepo("r1", owner)).thenReturn(
                List.of(new Branch("main", new Commit("sha1")))
        );

        var result = service.getRepos(owner);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("r1");
        assertThat(result.get(0).owner().login()).isEqualTo(owner);
        assertThat(result.get(0).branches()).hasSize(1);
        assertThat(result.get(0).branches().get(0).lastCommitSha()).isEqualTo("sha1");

        verify(api).getReposForOwner(owner);
        verify(api).getBranchesForRepo("r1", owner);
        verifyNoMoreInteractions(api);
    }

    @Test
    void returnsEmptyWhenAllReposAreForks() {
        var owner = "only-forks";
        when(api.getReposForOwner(owner)).thenReturn(List.of(
                new Repo("f1", new RepoOwner(owner), null, true),
                new Repo("f2", new RepoOwner(owner), null, true)
        ));

        var result = service.getRepos(owner);

        assertThat(result).isEmpty();
        verify(api, never()).getBranchesForRepo(anyString(), anyString());
    }

    @Test
    void mapsRepoWithNoBranchesToEmptyBranchList() {
        var owner = "octo";
        when(api.getReposForOwner(owner)).thenReturn(List.of(
                new Repo("empty", new RepoOwner(owner), null, false)
        ));
        when(api.getBranchesForRepo("empty", owner)).thenReturn(List.of());

        var result = service.getRepos(owner);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).branches()).isEmpty();
    }

    @Test
    void throwsWhenBranchHasNullCommit_currentBehavior() {
        var owner = "octo";
        when(api.getReposForOwner(owner)).thenReturn(List.of(
                new Repo("bad", new RepoOwner(owner), null, false)
        ));
        when(api.getBranchesForRepo("bad", owner)).thenReturn(List.of(
                new Branch("main", null)
        ));

        assertThrows(NullPointerException.class, () -> service.getRepos(owner));
    }
}