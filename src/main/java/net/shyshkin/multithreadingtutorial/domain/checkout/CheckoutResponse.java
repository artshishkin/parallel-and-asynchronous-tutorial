package net.shyshkin.multithreadingtutorial.domain.checkout;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutResponse {

    CheckoutStatus checkoutStatus;
    @Singular("error")
    List<CartItem> errorList = new ArrayList<>();
    double finalRate;

}
