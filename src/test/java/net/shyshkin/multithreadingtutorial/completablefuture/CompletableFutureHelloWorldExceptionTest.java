package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.service.HelloWorldService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldExceptionTest {

    @InjectMocks
    CompletableFutureHelloWorldException cfhwe;

    @Spy
    HelloWorldService helloWorldService = new HelloWorldService();

    @Test
    void helloWorld_3AsyncCall_handle1() {
        //given
        given(helloWorldService.hello()).willThrow(new RuntimeException("Hello Exception"));
        startTimer();

        //when
        String result =  cfhwe.helloWorld_3AsyncCall_handle();

        //then
        assertEquals("<RECOVER HELLO> WORLD! Hi! Completable Future!", result);
        timeTaken();
    }

    @Test
    void helloWorld_3AsyncCall_handle2() {
        //given
        given(helloWorldService.hello()).willThrow(new RuntimeException("Hello Exception"));
        given(helloWorldService.world()).willThrow(new RuntimeException("World Exception"));
        startTimer();

        //when
        String result =  cfhwe.helloWorld_3AsyncCall_handle();

        //then
        assertEquals("<RECOVER WORLD> Hi! Completable Future!", result);
        //We lost value from `hello`
        timeTaken();
    }

    @Test
    void helloWorld_3AsyncCall_handle3() {
        //given
        given(helloWorldService.world()).willThrow(new RuntimeException("World Exception"));
        startTimer();

        //when
        String result =  cfhwe.helloWorld_3AsyncCall_handle();

        //then
        assertEquals("<RECOVER WORLD> Hi! Completable Future!", result);
        //We lost value from `hello`
        timeTaken();
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}