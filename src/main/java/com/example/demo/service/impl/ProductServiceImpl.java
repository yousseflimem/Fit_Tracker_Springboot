package com.example.demo.service.impl;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
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
        return products.map(this::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        logger.debug("Fetching product with id: {}", id);
        GymProduct product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        logger.info("Creating product: {}", request.name());
        try {
            GymProduct product = GymProduct.from(request);
            GymProduct savedProduct = productRepository.save(product);
            logger.info("Product created with id: {}", savedProduct.getId());
            return toProductResponse(savedProduct);
        } catch (Exception e) {
            logger.error("Failed to create product: {}", request.name(), e);
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        logger.info("Updating product with id: {}", id);
        try {
            GymProduct product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
            product.update(request);
            GymProduct savedProduct = productRepository.save(product);
            logger.info("Product updated with id: {}", id);
            return toProductResponse(savedProduct);
        } catch (Exception e) {
            logger.error("Failed to update product with id: {}", id, e);
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Deleting product with id: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        logger.info("Product deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getLowStockProducts(int threshold, int page, int size) {
        logger.debug("Fetching low stock products with threshold: {}, page: {}, size: {}", threshold, page, size);
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        return productRepository.findByStockLessThan(threshold, pageRequest).map(this::toProductResponse);
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
        GymProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        int newStock = product.getStock() + quantityChange;
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative for product id: " + productId);
        }
        product.setStock(newStock);
        productRepository.save(product);
        logger.info("Stock updated for product id: {}", productId);
    }

    private ProductResponse toProductResponse(GymProduct product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                getTotalSoldQuantity(product.getId()),
                product.getCategory(),
                product.getImages().stream().map(Image::getUrl).toList()
        );
    }

    @Override
    @Transactional
    public void setStock(Long productId, int newStock) {
        logger.info("Setting stock for product id: {} to {}", productId, newStock);
        GymProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        if (newStock < 0) {
            logger.warn("Attempted to set negative stock for product id: {}", productId);
            throw new IllegalArgumentException("Stock cannot be negative for product id: " + productId);
        }
        product.setStock(newStock);
        productRepository.save(product);
        logger.info("Stock set for product id: {} to {}", productId, newStock);
    }
}