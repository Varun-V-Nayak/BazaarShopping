package com.example.final_UI_dev.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

//    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Products> products;

    public Brand(Integer brandId, String name, String imageUrl) {
        this.brandId = brandId;
        this.name = name;
        this.imageUrl = imageUrl;
       // this.products = products;
    }

    public Brand() {
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /*public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }*/
}

