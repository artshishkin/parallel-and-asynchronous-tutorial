package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.service.HelloWorldService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        String result = cfhwe.helloWorld_3AsyncCall_handle();

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
        String result = cfhwe.helloWorld_3AsyncCall_handle();

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
        String result = cfhwe.helloWorld_3AsyncCall_handle();

        //then
        assertEquals("<RECOVER WORLD> Hi! Completable Future!", result);
        //We lost value from `hello`
        timeTaken();
    }

    @Test
    void helloWorld_3AsyncCall_exceptionally_happyPath() {
        //given
        startTimer();

        //when
        String result = cfhwe.helloWorld_3AsyncCall_exceptionally();

        //then
        assertEquals("HELLO WORLD! Hi! Completable Future!", result);
        timeTaken();
    }

    @Test
    void helloWorld_3AsyncCall_exceptionally_helloEx() {
        //given
        given(helloWorldService.hello()).willThrow(new RuntimeException("Hello Exception"));
        startTimer();

        //when
        String result = cfhwe.helloWorld_3AsyncCall_exceptionally();

        //then
        assertEquals("<RECOVER HELLO> WORLD! Hi! Completable Future!", result);
        timeTaken();
    }

    @Test
    void helloWorld_3AsyncCall_exceptionally_worldEx() {
        //given
        given(helloWorldService.world()).willThrow(new RuntimeException("World Exception"));
        startTimer();

        //when
        String result = cfhwe.helloWorld_3AsyncCall_exceptionally();

        //then
        assertEquals("<RECOVER WORLD> Hi! Completable Future!", result);
        //We lost value from `hello`
        timeTaken();
    }

    @Test
    void helloWorld_3AsyncCall_exceptionally_doubleEx() {
        //given
        given(helloWorldService.hello()).willThrow(new RuntimeException("Hello Exception"));
        given(helloWorldService.world()).willThrow(new RuntimeException("World Exception"));
        startTimer();

        //when
        String result = cfhwe.helloWorld_3AsyncCall_exceptionally();

        //then
        assertEquals("<RECOVER WORLD> Hi! Completable Future!", result);
        //We lost value from `hello`
        timeTaken();
    }

    @Nested
    class WhenComplete {

        Supplier<BDDMockito.BDDMyOngoingStubbing<String>> hello_exception = () ->
                given(helloWorldService.hello()).willThrow(new RuntimeException("Hello Exception"));
        Supplier<BDDMockito.BDDMyOngoingStubbing<String>> world_exception = () ->
                given(helloWorldService.world()).willThrow(new RuntimeException("World Exception"));

        @Test
        void helloWorld_3AsyncCall_whenComplete_happyPath() {
            //given
            startTimer();

            //when
            String result = cfhwe.helloWorld_3AsyncCall_whenComplete();

            //then
            assertEquals("HELLO WORLD! Hi! Completable Future!", result);
            timeTaken();
        }

        @Test
        void helloWorld_3AsyncCall_whenComplete_helloEx() {
            //given
            startTimer();
            hello_exception.get();

            //when
            String result = cfhwe.helloWorld_3AsyncCall_whenComplete();

            //then
            assertEquals("<RECOVER HELLO> WORLD! Hi! Completable Future!", result);
            timeTaken();
        }

        @Test
        void helloWorld_3AsyncCall_whenComplete_worldEx() {
            //given
            world_exception.get();
            startTimer();

            //when
            Executable executable = () -> {
                String result = cfhwe.helloWorld_3AsyncCall_whenComplete();
            };

            //then
            assertThrows(RuntimeException.class, executable);

            timeTaken();
        }

        @Test
        void helloWorld_3AsyncCall_whenComplete_doubleEx() {
            //given
            hello_exception.get();
            world_exception.get();
            startTimer();

            //when
            Executable executable = () -> {
                String result = cfhwe.helloWorld_3AsyncCall_whenComplete();
            };

            //then
            assertThrows(RuntimeException.class, executable);

            timeTaken();
        }
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}