package com.example.final_UI_dev.repository;


import com.example.final_UI_dev.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

    List<Products> findByNameContainingIgnoreCase(String name);
    @Query("SELECT p.name FROM Products p")
    List<String> findAllProductNames();

}

