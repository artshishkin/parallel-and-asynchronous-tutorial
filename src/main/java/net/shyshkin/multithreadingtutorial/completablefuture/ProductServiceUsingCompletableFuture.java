package net.shyshkin.multithreadingtutorial.completablefuture;

import lombok.RequiredArgsConstructor;
import net.shyshkin.multithreadingtutorial.domain.Product;
import net.shyshkin.multithreadingtutorial.service.ProductInfoService;
import net.shyshkin.multithreadingtutorial.service.ReviewService;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;
import net.shyshkin.multithreadingtutorial.util.LoggerUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public class ProductServiceUsingCompletableFuture {
    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;

    public Product retrieveProductDetails(String productId) {
        CommonUtil.stopWatch.start();

        var productInfoCF = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        var reviewCF = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoCF
                .thenCombine(reviewCF,
                        (productInfo, review) -> new Product(productId, productInfo, review))
                .join();

        CommonUtil.stopWatch.stop();
        LoggerUtil.log("Total Time Taken : " + CommonUtil.stopWatch.getTime());
        return product;
    }

    public CompletableFuture<Product> retrieveProductDetailsAsync(String productId) {

        var productInfoCF = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        var reviewCF = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        return productInfoCF
                .thenCombine(reviewCF,
                        (productInfo, review) -> new Product(productId, productInfo, review));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        LoggerUtil.log("Product is " + product);

    }
}
