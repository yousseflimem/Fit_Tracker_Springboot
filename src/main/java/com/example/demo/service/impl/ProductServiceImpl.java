package com.example.demo.service.impl;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.GymProduct;
import com.example.demo.model.entity.Image;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public ProductServiceImpl(ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String keyword, int page, int size) {
        logger.debug("Searching products with keyword: {}, page: {}, size: {}", keyword, page, size);
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        Page<GymProduct> products = productRepository
                .findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(keyword, keyword, pageRequest);

        // Batch fetch sold quantities for all products in the page
        List<Long> productIds = products.getContent().stream()
                .map(GymProduct::getId)
                .collect(Collectors.toList());

        Map<Long, Integer> soldQuantities = new HashMap<>();
        if (!productIds.isEmpty()) {
            orderItemRepository.findTotalSoldQuantitiesByProductIds(productIds)
                    .forEach(result -> soldQuantities.put(result.getProductId(), result.getTotalQuantity()));
        }

        return products.map(product -> toProductResponse(product, soldQuantities.getOrDefault(product.getId(), 0)));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        logger.debug("Fetching product with id: {}", id);
        GymProduct product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return toProductResponse(product, getTotalSoldQuantity(id));
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        logger.info("Creating product: {}", request.name());
        try {
            GymProduct product = GymProduct.from(request);
            GymProduct savedProduct = productRepository.save(product);
            logger.info("Product created with id: {}", savedProduct.getId());
            return toProductResponse(savedProduct, 0); // New product has 0 sold quantity
        } catch (Exception e) {
            logger.error("Failed to create product: {}", request.name(), e);
            throw new ProductNotFoundException("Failed to create product: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        logger.info("Updating product with id: {}", id);
        try {
            GymProduct product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
            product.update(request);
            GymProduct savedProduct = productRepository.save(product);
            logger.info("Product updated with id: {}", id);
            return toProductResponse(savedProduct, getTotalSoldQuantity(id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update product with id: {}", id, e);
            throw new ProductNotFoundException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Deleting product with id: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        logger.info("Product deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getLowStockProducts(int threshold, int page, int size) {
        logger.debug("Fetching low stock products with threshold: {}, page: {}, size: {}", threshold, page, size);
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        Page<GymProduct> products = productRepository.findByStockLessThan(threshold, pageRequest);

        // Batch fetch sold quantities for all products in the page
        List<Long> productIds = products.getContent().stream()
                .map(GymProduct::getId)
                .collect(Collectors.toList());

        Map<Long, Integer> soldQuantities = new HashMap<>();
        if (!productIds.isEmpty()) {
            orderItemRepository.findTotalSoldQuantitiesByProductIds(productIds)
                    .forEach(result -> soldQuantities.put(result.getProductId(), result.getTotalQuantity()));
        }

        return products.map(product -> toProductResponse(product, soldQuantities.getOrDefault(product.getId(), 0)));
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalSoldQuantity(Long productId) {
        Integer totalSold = orderItemRepository.getTotalSoldQuantity(productId);
        return totalSold != null ? totalSold : 0;
    }

    @Override
    @Transactional
    public void updateStock(Long productId, int quantityChange) {
        logger.info("Updating stock for product id: {} by {}", productId, quantityChange);
        try {
            // Check if product exists before updating
            if (!productRepository.existsById(productId)) {
                throw new ProductNotFoundException("Product not found with id: " + productId);
            }

            // Use the repository method for better consistency
            productRepository.updateStockQuantity(productId, quantityChange);

            // Check if the update would result in negative stock
            GymProduct product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
            if (product.getStock() < 0) {
                // Revert the change if it resulted in negative stock
                productRepository.updateStockQuantity(productId, -quantityChange);
                throw new IllegalArgumentException("Stock cannot be negative for product id: " + productId);
            }

            logger.info("Stock updated for product id: {}", productId);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update stock for product id: {}", productId, e);
            throw new ProductNotFoundException("Failed to update stock: " + e.getMessage(), e);
        }
    }

    private ProductResponse toProductResponse(GymProduct product, int totalSoldQuantity) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                totalSoldQuantity,
                product.getCategory(),
                product.getImages().stream().map(Image::getUrl).toList()
        );
    }

    @Override
    @Transactional
    public void setStock(Long productId, int newStock) {
        logger.info("Setting stock for product id: {} to {}", productId, newStock);
        if (newStock < 0) {
            logger.warn("Attempted to set negative stock for product id: {}", productId);
            throw new IllegalArgumentException("Stock cannot be negative for product id: " + productId);
        }

        try {
            GymProduct product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
            product.setStock(newStock);
            productRepository.save(product);
            logger.info("Stock set for product id: {} to {}", productId, newStock);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to set stock for product id: {}", productId, e);
            throw new ProductNotFoundException("Failed to set stock: " + e.getMessage(), e);
        }
    }
}
