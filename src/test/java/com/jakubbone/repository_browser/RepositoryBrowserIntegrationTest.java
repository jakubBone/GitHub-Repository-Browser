package com.jakubbone.repository_browser;

import com.jakubbone.repository_browser.dto.ErrorResponse;
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
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class RepositoryBrowserIntegrationTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldReturnNonForkedRepositoriesWithBranchesForExistingOwner() {
		// Given: "octocat" selected as well-known GitHub account owner
		String existentOwner = "octocat";
		String forkedRepoName = "linguist";
		String url = "http://localhost:" + port + "/api/v1/repositories/" + existentOwner;

		// When: sending a GET request to the endpoint
		ResponseEntity<List<RepoResponse>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<RepoResponse>>() {
				}
		);

		// Then: validate business logic and response structure
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();

		List<RepoResponse> repos = response.getBody();
		assertThat(repos).isNotEmpty();

		// Validate each repository entry
		repos.forEach(repo -> {
			assertThat(repo.name()).isNotNull();
			assertThat(repo.owner()).isNotNull();
			assertThat(repo.owner().login()).isEqualTo(existentOwner);

			// Verify returned repositories are not forks
			assertThat(repo.name()).isNotEqualTo(forkedRepoName);

			// For each branch, verify structure
			repo.branches().forEach(branch -> {
				assertThat(branch.name()).isNotNull();
				assertThat(branch.lastCommitSha()).isNotNull();
			});
		});
	}

	@Test
	void shouldReturn404ForNonExistentOwner() {
		String nonExistingOwner = "notExistentTestOwner";
		String url = "http://localhost:" + port + "/api/v1/repositories/" + nonExistingOwner;

		ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody().statusCode()).isEqualTo(404);
		assertThat(response.getBody().message()).isEqualTo("Owner not found");
	}
}
