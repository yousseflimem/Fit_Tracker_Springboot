package com.example.demo.model.entity;

import com.example.demo.dto.request.ProductRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gym_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GymProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock = 0;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @OrderColumn(name = "image_order")
    private List<Image> images = new ArrayList<>();

    public static GymProduct from(ProductRequest productRequest) {
        GymProduct gymProduct = new GymProduct();
        gymProduct.setName(productRequest.name());
        gymProduct.setCategory(productRequest.category());
        gymProduct.setDescription(productRequest.description());
        gymProduct.setPrice(productRequest.price());
        gymProduct.setStock(productRequest.stock());
        List<Image> images = productRequest.imageUrls() != null ?
                productRequest.imageUrls().stream()
                        .filter(url -> url != null && !url.trim().isEmpty())
                        .map(url -> {
                            Image image = new Image();
                            image.setUrl(url);
                            return image;
                        })
                        .toList() : new ArrayList<>();
        gymProduct.setImages(images);
        return gymProduct;
    }

    public void update(ProductRequest productRequest) {
        this.name = productRequest.name();
        this.category = productRequest.category();
        this.description = productRequest.description();
        this.price = productRequest.price();
        this.stock = productRequest.stock();

        // Clear existing images to avoid persistence issues
        this.images.clear();

        // Add new images
        List<Image> images = productRequest.imageUrls() != null ?
                productRequest.imageUrls().stream()
                        .filter(url -> url != null && !url.trim().isEmpty())
                        .map(url -> {
                            Image image = new Image();
                            image.setUrl(url);
                            return image;
                        })
                        .toList() : new ArrayList<>();
        this.images.addAll(images);
    }
}