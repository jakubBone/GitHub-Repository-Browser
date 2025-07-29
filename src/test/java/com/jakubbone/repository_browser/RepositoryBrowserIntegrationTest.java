package com.jakubbone.repository_browser;

import com.jakubbone.repository_browser.dto.RepoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) class RepositoryBrowserIntegrationTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldReturnNonForkedRepositoryListWithBranchesForExistingOwner() {
		// Given: Known GitHub account owner who has both original repos and forks
		String existentOwner = "octocat";
		String forkedRepoName = "linguist"; // Known fork that should be filtered out

		String url = "http://localhost:" + port + "/api/v1/repositories/" + existentOwner;

		// When: Requesting repositories for the owner
		ResponseEntity<List<RepoResponse>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<RepoResponse>>() {
				}
		);

		// Then: Should return successful response with non-forked repos only
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();

		List<RepoResponse> repos = response.getBody();
		assertThat(repos).isNotEmpty();

		repos.forEach(repo -> {
			assertThat(repo.name()).isNotNull();
			assertThat(repo.owner().login()).isEqualTo(existentOwner);

			// Core business requirement: forks must be excluded
			assertThat(repo.name()).isNotEqualTo(forkedRepoName);

			// Each repo must have complete branch information
			repo.branches().forEach(branch -> {
				assertThat(branch.name()).isNotNull();
				assertThat(branch.lastCommitSha()).isNotNull();
			});
		});
	}
}
