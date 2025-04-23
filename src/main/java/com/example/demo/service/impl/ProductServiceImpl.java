package com.example.demo.service.impl;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.model.entity.GymProduct;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.service.ProductService;
import com.example.demo.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Page<ProductResponse> searchProducts(String keyword, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        Page<GymProduct> products = productRepository
                .findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                        keyword, keyword, pageRequest
                );
        return products.map(this::toProductResponse);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        GymProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toProductResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        GymProduct product = new GymProduct();
        updateProductFromRequest(product, request);
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        GymProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        updateProductFromRequest(product, request);
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductResponse> getLowStockProducts(int threshold, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        return productRepository.findByStockLessThan(threshold, pageRequest)
                .map(this::toProductResponse);
    }

    @Override
    public Integer getTotalSoldQuantity(Long productId) {
        return orderItemRepository.getTotalSoldQuantity(productId);
    }

    @Override
    @Transactional
    public void updateStock(Long productId, int quantityChange) {
        productRepository.updateStockQuantity(productId, quantityChange);
    }

    private void updateProductFromRequest(GymProduct product, ProductRequest request) {
        product.setName(request.name());
        product.setPrice(request.price());
        product.setDescription(request.description());
        product.setStock(request.stock());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());
    }

    private ProductResponse toProductResponse(GymProduct product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                getTotalSoldQuantity(product.getId()),  // Calculate total sold
                product.getCategory(),
                product.getImageUrl()
        );
    }

}