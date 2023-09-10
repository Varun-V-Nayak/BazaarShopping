package com.example.final_UI_dev.repository;

import com.example.final_UI_dev.entity.Cart;
import com.example.final_UI_dev.entity.Products;
import com.example.final_UI_dev.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(Users user);

    List<Cart> findByUser(Users users);

    Cart findByUserAndProduct(Users user, Products product);

    void deleteAllByUser(Users orElse);
}


