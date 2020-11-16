package net.shyshkin.multithreadingtutorial.apiclient;

import lombok.RequiredArgsConstructor;
import net.shyshkin.multithreadingtutorial.domain.github.GitHubPosition;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

@RequiredArgsConstructor
public class GitHubJobsClient {

    private final WebClient webClient;

    public List<GitHubPosition> invokeGithubJobsAPI_withPageNumber(int pageNum, String description) {

//       "https://jobs.github.com/positions.json?description=ruby&page=1";
        String uri = UriComponentsBuilder.fromPath("/positions.json")
                .queryParam("description", description)
                .queryParam("page", pageNum)
                .buildAndExpand().toUriString();
        log("uri is " + uri);
        List<GitHubPosition> gitHubPositions = webClient
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(GitHubPosition.class)
                .collectList()
                .block();
        return gitHubPositions;
    }

    public List<GitHubPosition> invokeGithubJobsAPI_usingMultiplePageNumbers(List<Integer> pageNumList, String description) {
        return pageNumList
                .stream()
                .map(pageNum -> invokeGithubJobsAPI_withPageNumber(pageNum, description))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<GitHubPosition> invokeGithubJobsAPI_usingMultiplePageNumbers_cf(List<Integer> pageNumList, String description) {
        List<CompletableFuture<List<GitHubPosition>>> collect = pageNumList
                .stream()
                .map(pageNum -> CompletableFuture.supplyAsync(() -> invokeGithubJobsAPI_withPageNumber(pageNum, description)))
                .collect(Collectors.toList());

        return collect.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<GitHubPosition> invokeGithubJobsAPI_usingMultiplePageNumbers_cf_allOf(List<Integer> pageNumList, String description) {
        List<CompletableFuture<List<GitHubPosition>>> gitHubPositions = pageNumList
                .stream()
                .map(pageNum -> CompletableFuture.supplyAsync(() -> invokeGithubJobsAPI_withPageNumber(pageNum, description)))
                .collect(Collectors.toList());

        CompletableFuture<Void> cfAllOf = CompletableFuture.allOf(gitHubPositions.toArray(new CompletableFuture[gitHubPositions.size()]));

        return cfAllOf.thenApply(v -> gitHubPositions.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()))
                .join();
    }

}
