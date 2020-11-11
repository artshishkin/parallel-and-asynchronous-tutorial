package net.shyshkin.multithreadingtutorial.service;

import net.shyshkin.multithreadingtutorial.domain.Review;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;

public class ReviewService {

    public Review retrieveReviews(String productId) {
        CommonUtil.delay(1000);
        return new Review(200, 4.5);
    }
}
