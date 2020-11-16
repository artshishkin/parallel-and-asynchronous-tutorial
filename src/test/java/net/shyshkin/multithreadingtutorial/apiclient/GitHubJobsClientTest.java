package net.shyshkin.multithreadingtutorial.apiclient;

import net.shyshkin.multithreadingtutorial.domain.github.GitHubPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GitHubJobsClientTest {

    WebClient webClient = WebClient.create("https://jobs.github.com");
    GitHubJobsClient gitHubJobsClient = new GitHubJobsClient(webClient);

    @Test
    void invokeGithubJobsAPI_withPageNumber() {
        //given
        final String description = "java";
        final int pageNum = 1;

        //when
        List<GitHubPosition> gitHubPositionList = gitHubJobsClient.invokeGithubJobsAPI_withPageNumber(pageNum, description);

        //then
        assertNotNull(gitHubPositionList);
        assertTrue(gitHubPositionList.size() > 2);
        gitHubPositionList.forEach(Assertions::assertNotNull);
    }
}