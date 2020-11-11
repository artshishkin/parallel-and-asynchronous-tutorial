package net.shyshkin.multithreadingtutorial.domain;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInfo {
    private String productId;
    @Singular
    private List<ProductOption> productOptions;
}
