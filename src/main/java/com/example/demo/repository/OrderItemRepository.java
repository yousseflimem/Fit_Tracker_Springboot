package com.example.demo.repository;

import com.example.demo.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product.id = :productId")
    Integer getTotalSoldQuantity(@Param("productId") Long productId);

    @Query("SELECT oi.product.id as productId, COALESCE(SUM(oi.quantity), 0) as totalQuantity " +
            "FROM OrderItem oi WHERE oi.product.id IN :productIds " +
            "GROUP BY oi.product.id")
    List<ProductSoldQuantityResult> findTotalSoldQuantitiesByProductIds(@Param("productIds") List<Long> productIds);

    interface ProductSoldQuantityResult {
        Long getProductId();
        Integer getTotalQuantity();
    }
}
