package com.example.clothes_api.controller;

import com.example.clothes_api.dto.product.ProductRequest;
import com.example.clothes_api.dto.product.ProductResponse;
import com.example.clothes_api.entity.Product;
import com.example.clothes_api.services.impl.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Product")
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "Add a new product",
            description = "Add a new product to the database",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product added successfully"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Invalid token / Unauthorized"
                    )
            }

    )
    @PreAuthorize("hasRole('ADMIN')")
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
