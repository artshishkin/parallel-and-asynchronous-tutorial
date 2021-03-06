package net.shyshkin.multithreadingtutorial.service;


import net.shyshkin.multithreadingtutorial.domain.checkout.CartItem;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;

import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

public class PriceValidatorService {

    public boolean isCartItemInvalid(CartItem cartItem) {
        int cartId = cartItem.getItemId();
        log("isCartItemInvalid : " + cartItem);
        CommonUtil.delay(500);
        if (cartId == 7 || cartId == 9 || cartId == 11) {
            return true;
        }
        return false;
    }
}
