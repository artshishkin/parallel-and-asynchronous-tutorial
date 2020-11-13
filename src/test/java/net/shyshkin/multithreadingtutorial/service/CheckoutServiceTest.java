package net.shyshkin.multithreadingtutorial.service;

import net.shyshkin.multithreadingtutorial.domain.checkout.Cart;
import net.shyshkin.multithreadingtutorial.domain.checkout.CheckoutResponse;
import net.shyshkin.multithreadingtutorial.domain.checkout.CheckoutStatus;
import net.shyshkin.multithreadingtutorial.util.DataSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.noOfCores;
import static net.shyshkin.multithreadingtutorial.util.CommonUtil.stopWatchReset;
import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    CheckoutService checkoutService = new CheckoutService(new PriceValidatorService());

    @Test
    void checkout_success_6_items() {
        //given
        Cart cart = DataSet.createCart(6);
        long maxDurationMs = calcMaxDurationMs(cart.getCartItemList().size());

        //when - then
        assertTimeoutPreemptively(Duration.ofMillis(maxDurationMs), () -> {
            CheckoutResponse checkoutResponse = checkoutService.checkout(cart);
            assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
            assertTrue(checkoutResponse.getFinalRate() > 0);
            assertTrue(checkoutResponse.getErrorList().isEmpty());
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 9, 13, 25})
    void checkout_failure(int noOfItemsInCart) {
        //given
        Cart cart = DataSet.createCart(noOfItemsInCart);
        long maxDurationMs = calcMaxDurationMs(noOfItemsInCart);
        int expectedErrorsCount = calcErrorsCount(noOfItemsInCart);

        //when - then
        assertTimeoutPreemptively(Duration.ofMillis(maxDurationMs), () -> {
            CheckoutResponse checkoutResponse = checkoutService.checkout(cart);
            assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
            assertEquals(0, checkoutResponse.getFinalRate());
            assertEquals(expectedErrorsCount, checkoutResponse.getErrorList().size());
        });
    }

    private int calcErrorsCount(int noOfItemsInCart) {
        return (int) Stream.of(7, 9, 11)
                .filter(errId -> errId <= noOfItemsInCart)
                .count();
    }

    private long calcMaxDurationMs(int noOfItemsInCart) {
        final int ONE_ITEM_DELAY_MS = 500;
        double durationCoefficient = 1.5;
        return (long) (durationCoefficient * ONE_ITEM_DELAY_MS * (noOfItemsInCart / noOfCores() + 1));
    }

    @Test
    void parallelism() {
        //given

        //when
        System.out.println("parallelism: " + ForkJoinPool.getCommonPoolParallelism());

        //then

    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}