package net.shyshkin.multithreadingtutorial.service;

import net.shyshkin.multithreadingtutorial.domain.checkout.Cart;
import net.shyshkin.multithreadingtutorial.domain.checkout.CheckoutResponse;
import net.shyshkin.multithreadingtutorial.domain.checkout.CheckoutStatus;
import net.shyshkin.multithreadingtutorial.util.DataSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.noOfCores;
import static net.shyshkin.multithreadingtutorial.util.CommonUtil.stopWatchReset;
import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    CheckoutService checkoutService = new CheckoutService(new PriceValidatorService());

    @Test
    void checkout_success_6_items() {
        //given
        Cart cart = DataSet.createCart(6);
        long maxDurationMs = getMaxDurationMs(cart);

        //when - then
        assertTimeoutPreemptively(Duration.ofMillis(maxDurationMs), () -> {
            CheckoutResponse checkoutResponse = checkoutService.checkout(cart);
            assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
            assertTrue(checkoutResponse.getFinalRate() > 0);
            assertTrue(checkoutResponse.getErrorList().isEmpty());
        });
    }

    @Test
    void checkout_failure_7_items() {
        //given
        Cart cart = DataSet.createCart(7);
        long maxDurationMs = getMaxDurationMs(cart);

        //when - then
        assertTimeoutPreemptively(Duration.ofMillis(maxDurationMs), () -> {
            CheckoutResponse checkoutResponse = checkoutService.checkout(cart);
            assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
            assertEquals(0, checkoutResponse.getFinalRate());
            assertEquals(1, checkoutResponse.getErrorList().size());
        });
    }

    private long getMaxDurationMs(Cart cart) {
        final int size = cart.getCartItemList().size();
        final int ONE_ITEM_DELAY_MS = 500;
        double durationCoefficient = 1.5;
        return (long) (durationCoefficient * ONE_ITEM_DELAY_MS * (size / noOfCores() + 1));
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}