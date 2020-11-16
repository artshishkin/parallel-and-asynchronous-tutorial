package net.shyshkin.multithreadingtutorial.apiclient;

import net.shyshkin.multithreadingtutorial.domain.github.GitHubPosition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
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

    @Test
    void invokeGithubJobsAPI_usingMultiplePageNumbers() {
        //given
        final String description = "java";
        List<Integer> pages = List.of(1, 2, 3, 4);
        startTimer();

        //when
        List<GitHubPosition> gitHubPositionList = gitHubJobsClient.invokeGithubJobsAPI_usingMultiplePageNumbers(pages, description);

        //then
        timeTaken();
        assertNotNull(gitHubPositionList);
        assertTrue(gitHubPositionList.size() > 2);
        gitHubPositionList.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGithubJobsAPI_usingMultiplePageNumbers_cf() {
        //given
        final String description = "java";
        List<Integer> pages = List.of(1, 2, 3, 4);
        startTimer();

        //when
        List<GitHubPosition> gitHubPositionList = gitHubJobsClient.invokeGithubJobsAPI_usingMultiplePageNumbers_cf(pages, description);

        //then
        timeTaken();
        assertNotNull(gitHubPositionList);
        assertTrue(gitHubPositionList.size() > 2);
        gitHubPositionList.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGithubJobsAPI_usingMultiplePageNumbers_cf_allOf() {
        //given
        final String description = "java";
        List<Integer> pages = List.of(1, 2, 3, 4);
        startTimer();

        //when
        List<GitHubPosition> gitHubPositionList = gitHubJobsClient.invokeGithubJobsAPI_usingMultiplePageNumbers_cf_allOf(pages, description);

        //then
        timeTaken();
        assertNotNull(gitHubPositionList);
        assertTrue(gitHubPositionList.size() > 2);
        gitHubPositionList.forEach(Assertions::assertNotNull);
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}