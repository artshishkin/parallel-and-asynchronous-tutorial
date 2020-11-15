package net.shyshkin.multithreadingtutorial.completablefuture;

import lombok.RequiredArgsConstructor;
import net.shyshkin.multithreadingtutorial.domain.Product;
import net.shyshkin.multithreadingtutorial.domain.ProductInfo;
import net.shyshkin.multithreadingtutorial.domain.ProductOption;
import net.shyshkin.multithreadingtutorial.domain.Review;
import net.shyshkin.multithreadingtutorial.service.InventoryService;
import net.shyshkin.multithreadingtutorial.service.ProductInfoService;
import net.shyshkin.multithreadingtutorial.service.ReviewService;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

@RequiredArgsConstructor
public class ProductServiceUsingCompletableFuture {
    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;
    private final InventoryService inventoryService;

    public Product retrieveProductDetails(String productId) {
        CommonUtil.stopWatch.start();

        var productInfoCF = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        var reviewCF = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoCF
                .thenCombine(reviewCF,
                        (productInfo, review) -> new Product(productId, productInfo, review))
                .join();

        CommonUtil.stopWatch.stop();
        log("Total Time Taken : " + CommonUtil.stopWatch.getTime());
        return product;
    }

    public CompletableFuture<Product> retrieveProductDetailsAsync(String productId) {

        var productInfoCF = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        var reviewCF = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        return productInfoCF
                .thenCombine(reviewCF,
                        (productInfo, review) -> new Product(productId, productInfo, review));
    }

    public CompletableFuture<Product> retrieveProductDetailsAsyncWithInventory(String productId) {

        var productInfoCF = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    updateInventory(productInfo);
                    return productInfo;
                });
        var reviewCF = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        return productInfoCF
                .thenCombine(reviewCF,
                        (productInfo, review) -> new Product(productId, productInfo, review));
    }

    public CompletableFuture<Product> retrieveProductDetailsAsyncWithInventory_approach2Async(String productId) {

        var productInfoCF = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventory_approach2(productInfo));
                    return productInfo;
                });
        var reviewCF = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId))
                .exceptionally(ex -> {
                    log("Handled Exception in the ReviewService: " + ex.getMessage());
                    return Review.builder().noOfReviews(0).overallRating(0.0).build();
                });

        return productInfoCF
                .thenCombine(reviewCF,
                        (productInfo, review) -> new Product(productId, productInfo, review));
    }

    public Product retrieveProductDetailsAsyncWithInventory_approach2(String productId) {

        var productInfoCF = CompletableFuture
                .supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventory_approach2(productInfo));
                    return productInfo;
                });
        var reviewCF = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId))
                .exceptionally(ex -> {
                    log("Handled Exception in the ReviewService: " + ex.getMessage());
                    return Review.builder().noOfReviews(0).overallRating(0.0).build();
                });

        return productInfoCF
                .thenCombine(reviewCF,
                        (productInfo, review) -> new Product(productId, productInfo, review))
                .whenComplete((product, ex) -> {
                    log("Inside WhenComplete: " + product + "; exception is: " + ex);
                })
                .join();
    }

    private void updateInventory(ProductInfo productInfo) {
        productInfo
                .getProductOptions()
                .parallelStream()
                .forEach(option -> option.setInventory(inventoryService.addInventory(option)));
    }

    private List<ProductOption> updateInventory_approach2(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> completableFutures = productInfo
                .getProductOptions()
                .stream()
                .map(productOption ->
                        CompletableFuture.supplyAsync(() -> inventoryService.addInventory(productOption))
                                .thenApply(inventory -> {
                                    productOption.setInventory(inventory);
                                    return productOption;
                                })
                )
                .collect(Collectors.toList());

        return completableFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        InventoryService inventoryService = new InventoryService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService, inventoryService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }
}
