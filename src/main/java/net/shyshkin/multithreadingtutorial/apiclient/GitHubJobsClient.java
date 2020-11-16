package net.shyshkin.multithreadingtutorial.apiclient;

import lombok.RequiredArgsConstructor;
import net.shyshkin.multithreadingtutorial.domain.github.GitHubPosition;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

}