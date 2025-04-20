package com.example.demo.controller;

import com.example.demo.entity.GymProduct;
import com.example.demo.repository.GymProductRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final GymProductRepository productRepository;

    public ProductController(GymProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<GymProduct> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping
    public GymProduct createProduct(@RequestBody GymProduct product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public GymProduct updateProduct(@PathVariable Long id, @RequestBody GymProduct product) {
        product.setId(id);
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
