package net.shyshkin.multithreadingtutorial.service;

import net.shyshkin.multithreadingtutorial.domain.Review;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.delay;

public class ReviewService {

    public Review retrieveReviews(String productId) {
        delay(1000);
        return Review.builder()
                .noOfReviews(200)
                .overallRating(4.5)
                .build();
    }
}
