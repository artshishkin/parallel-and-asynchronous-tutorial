package net.shyshkin.multithreadingtutorial.completablefuture;

import net.shyshkin.multithreadingtutorial.domain.Product;
import net.shyshkin.multithreadingtutorial.domain.ProductOption;
import net.shyshkin.multithreadingtutorial.service.InventoryService;
import net.shyshkin.multithreadingtutorial.service.ProductInfoService;
import net.shyshkin.multithreadingtutorial.service.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        CompletableFuture<Product> productCompletableFuture = psucf.retrieveProductDetailsAsyncWithInventory_approach2Async(productId);

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
        CompletableFuture<Product> productCF = psucf.retrieveProductDetailsAsyncWithInventory_approach2Async(productId);

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

    @Test
    void retrieveProductDetailsAsyncWithInventory_approach2_reviewServiceExSync() {
        //given
        String productId = "someId";
        given(reviewService.retrieveReviews(anyString())).willThrow(new RuntimeException("Review service exception"));
        startTimer();

        //when
        Product product = psucf.retrieveProductDetailsAsyncWithInventory_approach2(productId);

        //then
        assertAll(
                () -> assertNotNull(product.getReview()),
                () -> assertEquals(0, product.getReview().getNoOfReviews()),
                () -> assertEquals(0.0, product.getReview().getOverallRating())
        );
        timeTaken();
    }

    @Test
    void retrieveProductDetailsAsyncWithInventory_approach2_productInfoServiceEx() {
        //given
        String productId = "someId";
        given(productInfoService.retrieveProductInfo(anyString())).willThrow(new RuntimeException("Product info service exception"));
        startTimer();

        //when
        Executable executable = () -> {
            Product productCF = psucf.retrieveProductDetailsAsyncWithInventory_approach2(productId);
        };

        //then
        assertThrows(RuntimeException.class, executable);
        timeTaken();
    }

    @Test
    @DisplayName("When InventoryService throws exception we must handle it by passing Inventory with 1 element")
    void retrieveProductDetailsAsyncWithInventory_approach2_InventoryServiceEx() {
        //given
        String productId = "someId";
        given(inventoryService.addInventory(any(ProductOption.class))).willThrow(new RuntimeException("Inventory service exception"));
        startTimer();

        //when
        Product product = psucf.retrieveProductDetailsAsyncWithInventory_approach2(productId);

        //then
        assertAll(
                () -> assertFalse(product.getProductInfo().getProductOptions().isEmpty()),
                () -> assertNotNull(product.getReview()),
                () -> assertTrue(product.getReview().getNoOfReviews() > 0),

                //test goal
                () -> product.getProductInfo().getProductOptions()
                        .forEach(
                                productOption ->
                                        assertEquals(1, productOption.getInventory().getCount())
                        )
        );
        timeTaken();
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}