package com.example.clothes_api.controller;

import com.example.clothes_api.dto.OrderResponse;
import com.example.clothes_api.services.impl.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create-ghn")
    public ResponseEntity<OrderResponse> createOrderGHN() {;
        return new ResponseEntity<>(orderService.createOrderWithGHN().block(), HttpStatus.CREATED);
    }
}
