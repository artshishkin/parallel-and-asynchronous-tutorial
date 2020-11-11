package net.shyshkin.multithreadingtutorial.service;

import net.shyshkin.multithreadingtutorial.domain.ProductInfo;
import net.shyshkin.multithreadingtutorial.domain.ProductOption;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;

import java.util.List;

public class ProductInfoService {

    public ProductInfo retrieveProductInfo(String productId) {
        CommonUtil.delay(1000);
        List<ProductOption> productOptions = List.of(new ProductOption(1, "64GB", "Black", 699.99),
                new ProductOption(2, "128GB", "Black", 749.99));
        return ProductInfo.builder().productId(productId)
                .productOptions(productOptions)
                .build();
    }
}
