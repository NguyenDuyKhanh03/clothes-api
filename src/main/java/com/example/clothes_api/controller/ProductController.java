package com.example.clothes_api.controller;

import com.example.clothes_api.dto.product.ProductRequest;
import com.example.clothes_api.dto.product.ProductResponse;
import com.example.clothes_api.entity.Product;
import com.example.clothes_api.services.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @PostMapping("/add")
    public Product addProduct(ProductRequest product) {
        return productService.createProduct(product);
    }

    @DeleteMapping("/remove")
    public String removeProduct(@RequestParam Long id) {
         return productService.removeProduct(id);
    }

    @GetMapping("/get")
    public ProductResponse getProduct(@RequestParam Long id) {
        return productService.getProduct(id);
    }
}
