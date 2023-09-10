package com.example.final_UI_dev.service;

import com.example.final_UI_dev.entity.Products;
import com.example.final_UI_dev.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }

    public Optional<Products> getProductById(int productId) {
        return productsRepository.findById(productId);
    }

    public Products addProduct(Products product) {
        return productsRepository.save(product);
    }

    public void deleteProduct(int productId) {
        productsRepository.deleteById(productId);
    }

    public List<Products> searchProductsByName(String name) {
        List<Products> products = productsRepository.findByNameContainingIgnoreCase(name);
        if (name != null && !name.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return products;
    }
    public List<String> getAllProductNames() {
        return productsRepository.findAllProductNames();
    }

    public List<Products> getAllProductsByCategories(int categoriesId) {
        List<Products> productsList= productsRepository.findAll();
        List<Products> productsList1 = new ArrayList<>();
        for(Products products: productsList){
            if(products.getCategory().getCategoryId().equals(categoriesId)){
                productsList1.add(products);
            }

        }
        return productsList1;
    }
}
