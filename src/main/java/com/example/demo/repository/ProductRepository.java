package com.example.demo.repository;

import com.example.demo.model.entity.GymProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<GymProduct, Long> {

    // Find products by category with pagination
    Page<GymProduct> findByCategory(String category, Pageable pageable);

    // Search products by name or category
    Page<GymProduct> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
            String name,
            String category,
            Pageable pageable
    );

    // Find products with low stock (below specified threshold)
    Page<GymProduct> findByStockLessThan(int threshold, Pageable pageable);

    // Update stock quantity
    @Modifying
    @Query("UPDATE GymProduct p SET p.stock = p.stock + :quantity WHERE p.id = :productId")
    void updateStockQuantity(
            @Param("productId") Long productId,
            @Param("quantity") int quantity
    );
}