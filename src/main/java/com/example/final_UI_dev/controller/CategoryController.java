package com.example.final_UI_dev.controller;

import com.example.final_UI_dev.entity.Category;
import com.example.final_UI_dev.service.CategoryService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer categoryId) {
       Category category =  categoryService.getCategoryById(categoryId);
       return ResponseEntity.ok(category);
    }

    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

   /* @PutMapping("/{categoryId}")
    public Category updateCategory(@PathVariable Integer categoryId, @RequestBody Category category) {
        return categoryService.updateCategory(categoryId, category);
    }*/

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}

