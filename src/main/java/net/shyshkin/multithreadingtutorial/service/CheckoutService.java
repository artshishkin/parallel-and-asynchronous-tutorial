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

@RequiredArgsConstructor
public class CheckoutService {

    private final PriceValidatorService priceValidatorService;

    public CheckoutResponse checkout(Cart cart) {

        startTimer();

        List<CartItem> cartItemsExpired = cart.getCartItemList()
                .stream()
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

        double sum = cart.getCartItemList()
                .stream()
                .mapToDouble(item -> item.getRate() * item.getQuantity())
                .sum();

        timeTaken();
        return CheckoutResponse.builder()
                .checkoutStatus(SUCCESS)
                .finalRate(sum)
                .build();
    }
}
