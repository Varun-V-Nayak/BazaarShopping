package com.example.final_UI_dev.controller;

import com.example.final_UI_dev.entity.Review;
import com.example.final_UI_dev.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable Integer reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping("/product/{productId}")
    public List<Map<String, Object>> getReviewsByProductId(@PathVariable Integer productId) {
        return reviewService.getReviewsByProductId(productId);
    }
    @PostMapping("/{userId}/{productId}")
    public Review createReview(@PathVariable int userId, @PathVariable int productId,@RequestBody Review review) {
        return reviewService.save(userId,productId,review);
    }


    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @GetMapping("/{productId}/avg-rating")
    public double getAverageRatingForProduct(@PathVariable int productId) {
        return reviewService.getAverageRatingForProduct(productId);
    }

}
