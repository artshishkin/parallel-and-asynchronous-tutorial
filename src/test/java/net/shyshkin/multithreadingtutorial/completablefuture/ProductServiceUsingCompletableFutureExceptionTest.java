package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.domain.Product;
import net.shyshkin.multithreadingtutorial.service.InventoryService;
import net.shyshkin.multithreadingtutorial.service.ProductInfoService;
import net.shyshkin.multithreadingtutorial.service.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ProductServiceUsingCompletableFutureExceptionTest {

    @Spy
    ProductInfoService productInfoService;
    @Spy
    ReviewService reviewService;
    @Spy
    InventoryService inventoryService;

    @InjectMocks
    ProductServiceUsingCompletableFuture psucf;


    @Test
    void retrieveProductDetailsAsyncWithInventory_approach2_happyPath() {
        //given
        String productId = "someId";
        startTimer();

        //when
        CompletableFuture<Product> productCompletableFuture = psucf.retrieveProductDetailsAsyncWithInventory_approach2(productId);

        //then
        productCompletableFuture
                .thenAccept(product -> assertAll(
                        () -> assertNotNull(product),
                        () -> assertFalse(product.getProductInfo().getProductOptions().isEmpty()),
                        () -> assertNotNull(product.getReview()),
                        () -> assertTrue(product.getReview().getNoOfReviews() > 0),
                        () -> product.getProductInfo().getProductOptions()
                                .forEach(
                                        productOption ->
                                                assertEquals(2, productOption.getInventory().getCount())
                                )
                ))
                .join();
        timeTaken();
    }

    @Test
    void retrieveProductDetailsAsyncWithInventory_approach2_reviewServiceEx() {
        //given
        String productId = "someId";
        given(reviewService.retrieveReviews(anyString())).willThrow(new RuntimeException("Review service exception"));
        startTimer();

        //when
        CompletableFuture<Product> productCF = psucf.retrieveProductDetailsAsyncWithInventory_approach2(productId);

        //then
        productCF
                .thenAccept(product -> assertAll(
                        () -> assertNotNull(product.getReview()),
                        () -> assertEquals(0, product.getReview().getNoOfReviews()),
                        () -> assertEquals(0.0, product.getReview().getOverallRating())
                ))
                .join();
        timeTaken();
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}