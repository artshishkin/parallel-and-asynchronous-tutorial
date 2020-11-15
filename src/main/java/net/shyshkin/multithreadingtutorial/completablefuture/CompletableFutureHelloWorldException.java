package net.shyshkin.multithreadingtutorial.completablefuture;

import lombok.RequiredArgsConstructor;
import net.shyshkin.multithreadingtutorial.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.delay;
import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

@RequiredArgsConstructor
public class CompletableFutureHelloWorldException {

    private final HelloWorldService helloWorldService;

    public String helloWorld_3AsyncCall_handle() {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture
                .supplyAsync(() -> {
                    delay(900);
                    return " Hi! Completable Future!";
                });
        return hello
                .handle((res, ex) -> {//`handle` allows catch exception and recover
                    log("Exception is: " + ex.getMessage());
                    return "<recover hello>"; //recoverable value
                })
                .thenCombine(world, String::concat)
                .handle((res, ex) -> {
                    log("Exception after world is: " + ex.getMessage());
                    return "<recover world>";
                })
                .thenApply(String::toUpperCase)
                .thenCombine(hiCompletableFuture, String::concat)
                .join();
    }
}
