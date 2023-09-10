package com.example.final_UI_dev.service;

import com.example.final_UI_dev.entity.Products;
import com.example.final_UI_dev.entity.Review;
import com.example.final_UI_dev.repository.ProductsRepository;
import com.example.final_UI_dev.repository.ReviewRepository;
import com.example.final_UI_dev.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private UsersRepository usersRepository;

    public Review save(int userID,  int productId, Review review) {
        review.setProduct(productsRepository.findById(productId).orElse(null));
        review.setUser(usersRepository.findById(userID).orElse(null));
        review.setReviewDate(new Date()); // set the review date to the current date and time
        return reviewRepository.save(review);
    }


    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }


    public Review getReviewById(Integer id) {
        return reviewRepository.findById(id).orElse(null);
    }


    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }


/* public List<Map<String, Object>> getReviewsByProductId(Integer productId) {
     List<Review> allReviews = reviewRepository.findAll();
     List<Map<String, Object>> filteredReviews = new ArrayList<>();
     for (Review r : allReviews) {
         if (r.getProduct().getProductId() == productId) {
             Map<String, Object> reviewMap = new HashMap<>();
             reviewMap.put("reviewDate", r.getReviewDate());
             reviewMap.put("userName", r.getUser().getName());
             reviewMap.put("rating", r.getRating());
             reviewMap.put("comment", r.getComment());
             filteredReviews.add(reviewMap);
         }
     }
     return filteredReviews;
 }*/

    public List<Map<String, Object>> getReviewsByProductId(Integer productId) {
        List<Review> allReviews = reviewRepository.findAll();
        List<Map<String, Object>> filteredReviews = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Review r : allReviews) {
            if (r.getProduct().getProductId() == productId) {
                Map<String, Object> reviewMap = new HashMap<>();
                LocalDateTime reviewDate = r.getReviewDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
                long years = ChronoUnit.YEARS.between(reviewDate, now);
                long months = ChronoUnit.MONTHS.between(reviewDate, now);
                long days = ChronoUnit.DAYS.between(reviewDate, now);
                String formattedDate = "";

                if (years > 0) {
                    formattedDate = years + (years == 1 ? " year ago" : " years ago");
                } else if (months > 0) {
                    formattedDate = months + (months == 1 ? " month ago" : " months ago");
                } else if (days > 0) {
                    formattedDate = days + (days == 1 ? " day ago" : " days ago");
                } else {
                    formattedDate = "Today";
                }

                reviewMap.put("reviewDate", formattedDate);
                reviewMap.put("userName", r.getUser().getEmail());
                reviewMap.put("rating", r.getRating());
                reviewMap.put("comment", r.getComment());
                filteredReviews.add(reviewMap);
            }
        }
        return filteredReviews;
    }
    @Autowired
    private ProductsService productsService;
    public double getAverageRatingForProduct(int productId) {
        Products products = productsService.getProductById(productId).orElse(null);
        List<Review> reviews = reviewRepository.findByProduct(products);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }
}


