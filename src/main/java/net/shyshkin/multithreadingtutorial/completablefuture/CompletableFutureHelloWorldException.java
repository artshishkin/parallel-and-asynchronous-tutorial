package net.shyshkin.multithreadingtutorial.completablefuture;

import lombok.RequiredArgsConstructor;
import net.shyshkin.multithreadingtutorial.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
                    log("Result is: " + res);
                    if (ex != null) {
                        log("Exception is: " + ex.getMessage());
                        return "<recover hello>"; //recoverable value
                    } else {
                        return res;
                    }
                })
                .thenCombine(world, String::concat)
                .handle((res, ex) -> {
                    log("Result is: " + res);
                    if (ex != null) {
                        log("Exception after world is: " + ex.getMessage());
                        return "<recover world>"; //recoverable value
                    } else {
                        return res;
                    }
                })
                .thenApply(String::toUpperCase)
                .thenCombine(hiCompletableFuture, String::concat)
                .join();
    }

    public String helloWorld_3AsyncCall_exceptionally() {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture
                .supplyAsync(() -> {
                    delay(900);
                    return " Hi! Completable Future!";
                });
        return hello
                .exceptionally( ex -> {//`exceptionally` allows catch exception and recover
                        log("Exception is: " + ex.getMessage());
                        return "<recover hello>"; //recoverable value
                })
                .thenCombine(world, String::concat)
                .exceptionally( ex -> {
                    log("Exception after world is: " + ex.getMessage());
                    return "<recover world>"; //recoverable value
                })
                .thenApply(String::toUpperCase)
                .thenCombine(hiCompletableFuture, String::concat)
                .join();
    }

    public String helloWorld_3AsyncCall_whenComplete() {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture
                .supplyAsync(() -> {
                    delay(900);
                    return " Hi! Completable Future!";
                });
        BiConsumer<String, Throwable> whenCompleteConsumer = (res, ex) -> {
            log("Result is: " + res);
            if (ex != null) {
                log("Exception is: " + ex.getMessage());
            }
        };
        Function<Throwable, String> exceptionHandlerFunction = ex -> {
            log("Exception caught in EXCEPTIONALLY block is: " + ex.getMessage());
            return "<recover hello>";
        };
        return hello
                //`whenComplete` allows show exception
                .whenComplete(whenCompleteConsumer)
                .exceptionally(exceptionHandlerFunction)    //combine two exception methods
                .thenCombine(world, String::concat)
                .whenComplete(whenCompleteConsumer)
                .thenApply(String::toUpperCase)
                .thenCombine(hiCompletableFuture, String::concat)
                .join();
    }

}
