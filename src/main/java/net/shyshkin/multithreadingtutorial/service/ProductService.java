package net.shyshkin.multithreadingtutorial.service;

import net.shyshkin.multithreadingtutorial.domain.Product;
import net.shyshkin.multithreadingtutorial.domain.ProductInfo;
import net.shyshkin.multithreadingtutorial.domain.Review;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;
import net.shyshkin.multithreadingtutorial.util.LoggerUtil;

public class ProductService {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductService(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) {
        CommonUtil.stopWatch.start();

        ProductInfo productInfo = productInfoService.retrieveProductInfo(productId); // blocking call
        Review review = reviewService.retrieveReviews(productId); // blocking call

        CommonUtil.stopWatch.stop();
        LoggerUtil.log("Total Time Taken : "+ CommonUtil.stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductService productService = new ProductService(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        LoggerUtil.log("Product is " + product);

    }
}
