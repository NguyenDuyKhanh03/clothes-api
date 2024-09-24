package com.example.clothes_api.controller;

import com.example.clothes_api.entity.Cart;
import com.example.clothes_api.services.impl.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addProductToCart(Long productId, int quantity) {
        return new ResponseEntity<>(cartService.addProductToCart(productId, quantity), HttpStatus.CREATED);
    }

    @PostMapping("/remove")
    public ResponseEntity<Cart> removeProductFromCart(Long productId,int quantity) {
        return new ResponseEntity<>(cartService.removeProductFromCart(productId,quantity), HttpStatus.GONE);
    }

    @PostMapping("/update")
    public ResponseEntity<Cart> updateProductInCart(Long productId,int quantity) {
        return new ResponseEntity<>(cartService.incrementCartItem(productId,quantity), HttpStatus.OK);
    }
}
