package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    public static void main(String[] args) throws InterruptedException {

        HelloWorldService helloWorldService = new HelloWorldService();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        CompletableFuture
                .supplyAsync(() -> helloWorldService.helloWorld())
                .thenAccept(result -> {
                    log("Result is " + result);
                    countDownLatch.countDown();
                });

        log("main finished");

        countDownLatch.await();
//        delay(2000);
    }


}
