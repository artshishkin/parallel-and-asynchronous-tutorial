package net.shyshkin.multithreadingtutorial.apiclient;

import net.shyshkin.multithreadingtutorial.domain.github.GitHubPosition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GitHubJobsClientTest {

    static WebClient webClient = WebClient.create("https://jobs.github.com");
    static GitHubJobsClient gitHubJobsClient = new GitHubJobsClient(webClient);

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invokeGithubJobsAPI")
    void invokeGithubJobsAPI(String testName, BiFunction<List<Integer>, String, List<GitHubPosition>> function) {
        //given
        final String description = "java";
        List<Integer> pages = List.of(1, 2, 3, 4);
        startTimer();

        //when
        List<GitHubPosition> gitHubPositionList = function.apply(pages, description);

        //then
        timeTaken();
        assertNotNull(gitHubPositionList);
        assertTrue(gitHubPositionList.size() > 2);
        gitHubPositionList.forEach(Assertions::assertNotNull);
    }

    private static Stream<Arguments> invokeGithubJobsAPI() {

        Map<String, BiFunction<List<Integer>, String, List<GitHubPosition>>> functionMap = new LinkedHashMap<>();

        functionMap.put("one page",
                (pages, description) -> gitHubJobsClient.invokeGithubJobsAPI_withPageNumber(pages.get(0), description));
        functionMap.put("using stream()",
                (pages, description) -> gitHubJobsClient.invokeGithubJobsAPI_usingMultiplePageNumbers(pages, description));
        functionMap.put("using completable future",
                (pages, description) -> gitHubJobsClient.invokeGithubJobsAPI_usingMultiplePageNumbers_cf(pages, description));
        functionMap.put("using completable future and allOf()",
                (pages, description) -> gitHubJobsClient.invokeGithubJobsAPI_usingMultiplePageNumbers_cf_allOf(pages, description));

        return functionMap.entrySet().stream()
                .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}