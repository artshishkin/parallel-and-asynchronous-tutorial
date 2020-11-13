package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    public static void main(String[] args) {

        HelloWorldService helloWorldService = new HelloWorldService();

        CompletableFuture<Void> completableFuture = CompletableFuture
                .supplyAsync(() -> helloWorldService.helloWorld())
                .thenAccept(result -> log("Result is " + result));

        log("main finished");

        completableFuture.join();
//        delay(2000);
    }


}
