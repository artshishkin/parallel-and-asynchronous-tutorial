package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.service.HelloWorldService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CompletableFutureHelloWorldTest {

    CompletableFutureHelloWorld cfhw = new CompletableFutureHelloWorld(new HelloWorldService());

    @Test
    void helloWorld() {
        //given

        //when
        CompletableFuture<String> completableFuture = cfhw.helloWorld();

        //then
        completableFuture
                .thenAccept(result -> {
                    assertEquals("HELLO WORLD", result);
                })
                .join();

    }

    @Test
    void helloWorld_thenCompose() {
        //given
        startTimer();

        //when
        CompletableFuture<String> completableFuture = cfhw.helloWorld_thenCompose();

        //then
        completableFuture
                .thenAccept(result -> {
                    assertEquals("HELLO WORLD!", result);
                })
                .join();
        timeTaken();
    }

    @Test
    void helloWorld_withSize() {
        //when
        CompletableFuture<String> completableFuture = cfhw.helloWorld_withSize();

        //then
        completableFuture
                .thenAccept(result -> assertEquals("11 - HELLO WORLD", result))
                .join();
    }

    @Test
    void helloWorld_multipleAsyncCall() {
        //when
        startTimer();
        String helloWorld = cfhw.helloWorld_multipleAsyncCall();
        timeTaken();

        //then
        assertEquals("HELLO WORLD!", helloWorld);
    }

    @Test
    void helloWorld_3AsyncCall() {
        //when
        startTimer();
        String helloWorld = cfhw.helloWorld_3AsyncCall();
        timeTaken();

        //then
        assertEquals("HELLO WORLD! Hi! Completable Future!", helloWorld);
    }

    @Test
    void helloWorld_4_async_calls() {
        //when
        String helloWorld = cfhw.helloWorld_4_async_calls();

        //then
        assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE!  BYE!", helloWorld);
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}