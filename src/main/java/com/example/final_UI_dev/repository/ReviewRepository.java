package com.example.final_UI_dev.repository;

import com.example.final_UI_dev.entity.Products;
import com.example.final_UI_dev.entity.Review;
import com.example.final_UI_dev.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByUser(Users user);

    List<Review> findByProduct(Products products);

}

