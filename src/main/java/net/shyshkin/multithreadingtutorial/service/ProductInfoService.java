package net.shyshkin.multithreadingtutorial.service;

import net.shyshkin.multithreadingtutorial.domain.ProductInfo;
import net.shyshkin.multithreadingtutorial.domain.ProductOption;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.delay;

public class ProductInfoService {

    public ProductInfo retrieveProductInfo(String productId) {
        delay(1000);
        return ProductInfo.builder()
                .productId(productId)
                .productOption(ProductOption.builder().productionOptionId(1).size("64GB").color("Black").price(699.99).build())
                .productOption(new ProductOption(2, "128GB", "Black", 749.99))
                .productOption(new ProductOption(3, "128GB", "Black1", 749.99))
                .productOption(new ProductOption(4, "128GB", "Black2", 749.99))
                .build();
    }
}
