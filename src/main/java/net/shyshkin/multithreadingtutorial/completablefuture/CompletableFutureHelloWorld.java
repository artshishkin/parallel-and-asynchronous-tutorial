package net.shyshkin.multithreadingtutorial.completablefuture;

import lombok.RequiredArgsConstructor;
import net.shyshkin.multithreadingtutorial.service.HelloWorldService;
import net.shyshkin.multithreadingtutorial.util.LoggerUtil;

import java.util.concurrent.CompletableFuture;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

@RequiredArgsConstructor
public class CompletableFutureHelloWorld {

    private final HelloWorldService helloWorldService;

    public CompletableFuture<String> helloWorld() {
        return CompletableFuture
                .supplyAsync(helloWorldService::helloWorld)
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloWorld_withSize() {
        return helloWorld()
                .thenApply(str -> String.format("%d - %s", str.length(), str));
    }

    public String helloWorld_approach1() {
        String hello = helloWorldService.hello();
        String world = helloWorldService.world();
        return hello + " " + world;
    }

    public String helloWorld_multipleAsyncCall() {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        return hello.thenCombine(world, String::concat)
                .thenApply(String::toUpperCase)
                .join();
    }

    public String helloWorld_3AsyncCall() {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture
                .supplyAsync(() -> {
                    delay(900);
                    return " Hi! Completable Future!";
                });
        return hello
                .thenCombine(world, String::concat)
                .thenApply(String::toUpperCase)
                .thenCombine(hiCompletableFuture, String::concat)
                .join();
    }

    public String helloWorld_4_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> this.helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> this.helloWorldService.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " HI Completable Future!";
        });
        // Add the 4th CompletableFuture that returns a String "  Bye!"
        CompletableFuture<String> bye = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "  Bye!";
        });

        String hw = hello
                .thenCombine(world, String::concat) // (first,second)
                .thenCombine(hiCompletableFuture, String::concat)
                // Combine the fourth CompletableFuture
                .thenCombine(bye, String::concat)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return hw;
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
