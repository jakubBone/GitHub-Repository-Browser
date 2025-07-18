package com.jakubbone.github_repository_browser.config;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 The class is GitHub client instance
 The Builder first looks for environment variables e.g.GITHUB_OAUTH (for a token)
 If these are no variables it does not cause an error
 Instead it creates an anonymous (unauthenticated) connection
*/
@Configuration
public class GitHubApiConfig {
    @Bean
    public GitHub gitHub() throws IOException {
        return GitHubBuilder.fromEnvironment().build();
    };
}
