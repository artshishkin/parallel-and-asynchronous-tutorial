package net.shyshkin.multithreadingtutorial.completablefuture;

import lombok.RequiredArgsConstructor;
import net.shyshkin.multithreadingtutorial.service.HelloWorldService;
import net.shyshkin.multithreadingtutorial.util.LoggerUtil;

import java.util.concurrent.CompletableFuture;

import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

@RequiredArgsConstructor
public class CompletableFutureHelloWorld {

    private final HelloWorldService helloWorldService;

    public CompletableFuture<String> helloWorld() {
        return CompletableFuture
                .supplyAsync(helloWorldService::helloWorld)
                .thenApply(String::toUpperCase);
    }

    public static void main(String[] args) {

        HelloWorldService helloWorldService = new HelloWorldService();

        CompletableFuture<Void> completableFuture = CompletableFuture
                .supplyAsync(helloWorldService::helloWorld)
                .thenApply(String::toUpperCase)
                .thenApply("Result is "::concat)
                .thenAccept(LoggerUtil::log);

        log("main finished");

        completableFuture.join();
//        delay(2000);
    }


}
