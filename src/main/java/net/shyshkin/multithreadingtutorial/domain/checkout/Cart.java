package net.shyshkin.multithreadingtutorial.domain.checkout;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    private Integer cardId;
    @Singular("cartItem")
    private List<CartItem> cartItemList;

}
