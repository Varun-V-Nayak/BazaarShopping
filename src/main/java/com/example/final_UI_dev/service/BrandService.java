package com.example.final_UI_dev.service;

import com.example.final_UI_dev.entity.Brand;
import com.example.final_UI_dev.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand getBrandById(int id) {
        Brand brand = brandRepository.findById(id).orElse(null);
        return brand;
    }

    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public void deleteBrand(int id) {
        brandRepository.deleteById(id);
    }

    public void updateBrand(Brand brand) {
        brandRepository.save(brand);
    }
}

