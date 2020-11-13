package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

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
}