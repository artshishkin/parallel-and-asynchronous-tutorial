package net.shyshkin.multithreadingtutorial.service;

import lombok.RequiredArgsConstructor;
import net.shyshkin.multithreadingtutorial.domain.checkout.Cart;
import net.shyshkin.multithreadingtutorial.domain.checkout.CartItem;
import net.shyshkin.multithreadingtutorial.domain.checkout.CheckoutResponse;

import java.util.List;
import java.util.stream.Collectors;

import static net.shyshkin.multithreadingtutorial.domain.checkout.CheckoutStatus.FAILURE;
import static net.shyshkin.multithreadingtutorial.domain.checkout.CheckoutStatus.SUCCESS;
import static net.shyshkin.multithreadingtutorial.util.CommonUtil.startTimer;
import static net.shyshkin.multithreadingtutorial.util.CommonUtil.timeTaken;
import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

@RequiredArgsConstructor
public class CheckoutService {

    private final PriceValidatorService priceValidatorService;

    public CheckoutResponse checkout(Cart cart) {

        startTimer();

        List<CartItem> cartItemsExpired = cart.getCartItemList()
                .parallelStream()
                .filter(priceValidatorService::isCartItemInvalid)
                .peek(item -> item.setExpired(true))
                .collect(Collectors.toList());

        if (!cartItemsExpired.isEmpty()) {
            timeTaken();
            return CheckoutResponse.builder()
                    .checkoutStatus(FAILURE)
                    .errorList(cartItemsExpired)
                    .build();
        }

        double sum = calculateFinalPrice(cart);
//        double sum = calculateFinalPriceUsingReduce(cart);

        log("Checkout complete and the Final price is: " + sum);

        timeTaken();
        return CheckoutResponse.builder()
                .checkoutStatus(SUCCESS)
                .finalRate(sum)
                .build();
    }

    private double calculateFinalPrice(Cart cart) {
        return cart.getCartItemList()
                .stream()
                .mapToDouble(item -> item.getRate() * item.getQuantity())
                .sum();
    }

    private double calculateFinalPriceUsingCollect(Cart cart) {
        return cart.getCartItemList()
                .stream()
                .map(item -> item.getRate() * item.getQuantity())
                .collect(Collectors.summingDouble(Double::doubleValue));
    }

    private double calculateFinalPriceUsingReduce(Cart cart) {
        return cart.getCartItemList()
                .stream()
                .map(item -> item.getRate() * item.getQuantity())
                .reduce(0.0, Double::sum);
    }

}
