package com.example.final_UI_dev.controller;
import com.example.final_UI_dev.entity.Brand;
import com.example.final_UI_dev.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/")
    public ResponseEntity<?> getAllBrands() {
        try {
            List<Brand>brands = brandService.getAllBrands();
           return new ResponseEntity<>(brands, HttpStatus.OK);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable int id) {
        try {
            Brand brand = brandService.getBrandById(id);
            return new ResponseEntity<>(brand, HttpStatus.OK);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createBrand(@RequestBody Brand brand) {
        try {
            Brand brand1 = brandService.createBrand(brand);
            return new ResponseEntity<>(brand1, HttpStatus.OK);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable int id) {
        try {
            brandService.deleteBrand(id);
            return  ResponseEntity.ok("Brand deleted");
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public void updateBrand(@RequestBody Brand brand, @PathVariable int id) {
        Brand existingBrand = brandService.getBrandById(id);
        if (existingBrand != null) {
            brand.setBrandId(id);
            brandService.updateBrand(brand);
        }
    }

}