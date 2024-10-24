package com.example.clothes_api.controller;

import com.example.clothes_api.dto.product.ProductRequest;
import com.example.clothes_api.dto.product.ProductResponse;
import com.example.clothes_api.entity.Product;
import com.example.clothes_api.services.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(ProductRequest product) {
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeProduct(@RequestParam Long id) {
         return new ResponseEntity<>(productService.removeProduct(id),HttpStatus.GONE);
    }

    @GetMapping("/get")
    public ResponseEntity<ProductResponse> getProduct(@RequestParam Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProductByTitle(@RequestParam String title) {
        return ResponseEntity.ok(productService.searchProductByTitle(title));
    }
}
