package com.example.demo.model.entity;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.exception.BusinessRuleException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gym_products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class GymProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;
    private String description;
    private Double price;
    private String imageUrl;

    @Column(nullable = false)
    private Integer stock = 0;



    public static GymProduct from(ProductRequest productRequest) {
        GymProduct gymProduct = new GymProduct();
        gymProduct.setName(productRequest.name());
        gymProduct.setCategory(productRequest.category());
        gymProduct.setDescription(productRequest.description());
        gymProduct.setPrice(productRequest.price());
        gymProduct.setStock(productRequest.stock());  // Added stock mapping
        gymProduct.setImageUrl(productRequest.imageUrl());
        return gymProduct;
    }

    public void update(ProductRequest productRequest) {
        this.name = productRequest.name();
        this.category = productRequest.category();
        this.description = productRequest.description();
        this.price = productRequest.price();
        this.stock = productRequest.stock();  // Added stock update
        this.imageUrl = productRequest.imageUrl();
    }
}