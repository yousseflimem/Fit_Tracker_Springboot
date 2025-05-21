package com.example.demo.model.entity;

import com.example.demo.dto.request.ProductRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<Image> images = productRequest.imageUrls().stream()
                .filter(url -> url != null && !url.trim().isEmpty())
                .map(url -> {
                    Image image = new Image();
                    image.setUrl(url);
                    return image;
                })
                .toList();

        gymProduct.setImages(images);
        return gymProduct;
    }

    public void update(ProductRequest productRequest) {
        this.name = productRequest.name();
        this.category = productRequest.category();
        this.description = productRequest.description();
        this.price = productRequest.price();
        this.stock = productRequest.stock();

        // More efficient image update strategy
        if (productRequest.imageUrls() == null || productRequest.imageUrls().isEmpty()) {
            // Clear all images if new list is empty
            this.images.clear();
        } else {
            // Get new image URLs
            List<String> newUrls = productRequest.imageUrls().stream()
                    .filter(url -> url != null && !url.trim().isEmpty())
                    .toList();

            // Remove images that are not in the new list
            this.images.removeIf(image -> !newUrls.contains(image.getUrl()));

            // Add new images that don't exist yet
            List<String> existingUrls = this.images.stream()
                    .map(Image::getUrl)
                    .toList();

            newUrls.stream()
                    .filter(url -> !existingUrls.contains(url))
                    .forEach(url -> {
                        Image image = new Image();
                        image.setUrl(url);
                        this.images.add(image);
                    });
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GymProduct that = (GymProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
