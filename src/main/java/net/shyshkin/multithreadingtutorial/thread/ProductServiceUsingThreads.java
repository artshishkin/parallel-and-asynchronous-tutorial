package net.shyshkin.multithreadingtutorial.thread;

import lombok.Getter;
import net.shyshkin.multithreadingtutorial.domain.Product;
import net.shyshkin.multithreadingtutorial.domain.ProductInfo;
import net.shyshkin.multithreadingtutorial.domain.Review;
import net.shyshkin.multithreadingtutorial.service.ProductInfoService;
import net.shyshkin.multithreadingtutorial.service.ReviewService;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;
import net.shyshkin.multithreadingtutorial.util.LoggerUtil;

public class ProductServiceUsingThreads {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceUsingThreads(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException {
        CommonUtil.stopWatch.start();

        ProductInfoServiceRunnable productInfoServiceRunnable = new ProductInfoServiceRunnable(productId);
        Thread productInfoServiceThread = new Thread(productInfoServiceRunnable);
        ReviewServiceRunnable reviewServiceRunnable = new ReviewServiceRunnable(productId);
        Thread reviewServiceThread = new Thread(reviewServiceRunnable);
        productInfoServiceThread.start();
        reviewServiceThread.start();

        productInfoServiceThread.join();
        reviewServiceThread.join();

        ProductInfo productInfo = productInfoServiceRunnable.getProductInfo();
        Review review = reviewServiceRunnable.getReview();

        CommonUtil.stopWatch.stop();
        LoggerUtil.log("Total Time Taken : "+ CommonUtil.stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingThreads productService = new ProductServiceUsingThreads(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        LoggerUtil.log("Product is " + product);

    }

    private class ProductInfoServiceRunnable implements Runnable{
        private String productId;
        private ProductInfo productInfo;

        public ProductInfoServiceRunnable(String productId) {
            this.productId = productId;
        }

        @Override
        public void run() {
            // blocking call
            productInfo = productInfoService.retrieveProductInfo(productId);
        }

        public ProductInfo getProductInfo() {
            return productInfo;
        }
    }

    private class ReviewServiceRunnable implements Runnable{
        private String productId;
        @Getter
        private Review review;

        public ReviewServiceRunnable(String productId) {
            this.productId = productId;
        }

        @Override
        public void run() {
            // blocking call
            review = reviewService.retrieveReviews(productId);
        }
    }
}
