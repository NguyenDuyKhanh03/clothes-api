package com.example.clothes_api.controller;

import com.example.clothes_api.dto.CartDetailResponse;
import com.example.clothes_api.dto.CartDetailsRequest;
import com.example.clothes_api.dto.CartResponse;
import com.example.clothes_api.services.impl.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProductToCart(@RequestBody CartDetailsRequest request) {
        return new ResponseEntity<>(cartService.addProductToCart(request), HttpStatus.CREATED);
    }

    @PostMapping("/remove")
    public ResponseEntity<CartResponse> removeProductFromCart(@RequestParam(name = "product_id") Long productId,@RequestParam int quantity) {
        return new ResponseEntity<>(cartService.removeProductFromCart(productId,quantity), HttpStatus.GONE);
    }

    @PostMapping("/update")
    public ResponseEntity<CartResponse> updateProductInCart(@RequestParam(name = "product_id") Long productId,@RequestParam int quantity) {
        return new ResponseEntity<>(cartService.incrementCartItem(productId,quantity), HttpStatus.OK);
    }

    @GetMapping("/get-cart-details")
    public ResponseEntity<List<CartDetailResponse>> getCartDetails() {
        return new ResponseEntity<>(cartService.getCartDetails(), HttpStatus.OK);
    }
}
