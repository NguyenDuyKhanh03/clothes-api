package com.example.clothes_api.controller;

import com.example.clothes_api.dto.OrderResponse;
import com.example.clothes_api.services.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder() {
        return new ResponseEntity<>(orderService.createOrder(), HttpStatus.CREATED);
    }
}
