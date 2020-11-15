package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.domain.Product;
import net.shyshkin.multithreadingtutorial.service.InventoryService;
import net.shyshkin.multithreadingtutorial.service.ProductInfoService;
import net.shyshkin.multithreadingtutorial.service.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {

    ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(
            new ProductInfoService(), new ReviewService(), new InventoryService());

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

    @Test
    void retrieveProductDetailsAsync() {
        //given
        String productId = "someId";
        startTimer();

        assertTimeoutPreemptively(Duration.ofMillis(1100), () -> {

            //when
            CompletableFuture<Product> productCF = productService.retrieveProductDetailsAsync(productId);

            //then
            productCF
                    .thenAccept(product ->
                            assertAll(
                                    () -> assertNotNull(product),
                                    () -> assertFalse(product.getProductInfo().getProductOptions().isEmpty()),
                                    () -> assertNotNull(product.getReview())
                            ))
                    .join();

            timeTaken();
        });
    }

    @Test
    void retrieveProductDetailsAsyncWithInventory() {
        //given
        String productId = "someId";
        startTimer();

        assertTimeoutPreemptively(Duration.ofMillis(1700), () -> {

            //when
            CompletableFuture<Product> productCF = productService.retrieveProductDetailsAsyncWithInventory(productId);

            //then
            productCF
                    .thenAccept(product ->
                            assertAll(
                                    () -> assertNotNull(product),
                                    () -> assertFalse(product.getProductInfo().getProductOptions().isEmpty()),
                                    () -> assertNotNull(product.getReview()),
                                    () -> product.getProductInfo().getProductOptions()
                                            .forEach(
                                                    productOption ->
                                                            assertEquals(2, productOption.getInventory().getCount())
                                            )
                            ))
                    .join();

            timeTaken();
        });
    }

    @Test
    void updateInventory_approach2() {
        //given
        String productId = "someId";
        startTimer();

        assertTimeout(Duration.ofMillis(1700), () -> {

            //when
            CompletableFuture<Product> productCF = productService.retrieveProductDetailsAsyncWithInventory_approach2(productId);

            //then
            productCF
                    .thenAccept(product ->
                            assertAll(
                                    () -> assertNotNull(product),
                                    () -> assertFalse(product.getProductInfo().getProductOptions().isEmpty()),
                                    () -> assertNotNull(product.getReview()),
                                    () -> product.getProductInfo().getProductOptions()
                                            .forEach(
                                                    productOption ->
                                                            assertEquals(2, productOption.getInventory().getCount())
                                            )
                            ))
                    .join();

            timeTaken();
        });
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}