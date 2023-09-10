package com.example.final_UI_dev.controller;

import com.example.final_UI_dev.entity.Products;
import com.example.final_UI_dev.entity.Review;
import com.example.final_UI_dev.repository.ProductsRepository;
import com.example.final_UI_dev.repository.ReviewRepository;
import com.example.final_UI_dev.service.ProductsService;
import com.example.final_UI_dev.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductsService productsService;

    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {
        List<Products> products = productsService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/category/{categoriesId}")
    public ResponseEntity<List<Products>> getAllProductsByCategoriesId(@PathVariable int categoriesId) {
        List<Products> products = productsService.getAllProductsByCategories(categoriesId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable int productId) {

        List<Review> reviews = reviewRepository.findByProduct(productsRepository.findById(productId).orElse(null));
        Map<String, Object> response = new HashMap<>();
        Optional<Products> optionalProduct = productsRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Products product = optionalProduct.get();
            response.put("productId", product.getProductId());
            response.put("name", product.getName());
            response.put("description", product.getDescription());
            response.put("price", product.getPrice());
            response.put("imageUrl", product.getImageUrl());
            response.put("stock", product.getStock());
            response.put("brand", product.getBrand().getName());
            response.put("rating",Math.round(reviewService.getAverageRatingForProduct(productId) * 100.0) / 100.0);
            List<Map<String, Object>> filteredReviews = new ArrayList<>();

            for (Review review : reviews) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime reviewDate = review.getReviewDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
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
                Map<String, Object> filteredReview = new HashMap<>();
                double rating = review.getRating();
                filteredReview.put("rating", rating);
                filteredReview.put("comment", review.getComment());
                filteredReview.put("reviewDate", formattedDate);
                filteredReview.put("userEmail", review.getUser().getEmail());
                filteredReviews.add(filteredReview);
            }
            response.put("reviews", filteredReviews);
            response.put("Number of reviews", filteredReviews.size()); // to get number of reviews
            //response.put("reviews",reviews);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<Products> addProduct(@RequestBody Products product) {
        Products addedProduct = productsService.addProduct(product);
        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int productId) {
        productsService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/search")          //search bar
    public List<Products> searchProductsByName(@RequestParam("name") String name) {
        return productsService.searchProductsByName(name);
    }
    @GetMapping("/names")       // return list of product names for filtering in search bar
    public ResponseEntity<List<String>> getAllProductNames() {
        List<String> productNames = productsService.getAllProductNames();
        return ResponseEntity.ok(productNames);
    }

}

