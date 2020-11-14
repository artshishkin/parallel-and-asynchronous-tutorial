package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.domain.Product;
import net.shyshkin.multithreadingtutorial.service.ProductInfoService;
import net.shyshkin.multithreadingtutorial.service.ReviewService;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {

    ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(new ProductInfoService(), new ReviewService());

    @Test
    void retrieveProductDetails() {
        //given
        String productId = "someId";

        assertTimeoutPreemptively(Duration.ofMillis(1100), () -> {

            //when
            Product product = productService.retrieveProductDetails(productId);

            //then
            assertAll(
                    () -> assertNotNull(product),
                    () -> assertFalse(product.getProductInfo().getProductOptions().isEmpty()),
                    () -> assertNotNull(product.getReview())
            );
        });

    }
}