package com.example.clothes_api.controller;

import com.example.clothes_api.dto.payment.PaymentRequest;
import com.example.clothes_api.entity.Payment;
import com.example.clothes_api.services.impl.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-zalo")
    public Object createPaymentZALO(@RequestBody PaymentRequest request) {
        return paymentService.createPaymentZALOPAY(request).block();
    }


    @PostMapping("/create")
    public Payment createPayment(@RequestBody PaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @PostMapping("/status")
    public Integer getPaymentStatus(@RequestBody String request) {
        return paymentService.getStatusPaymentZALO(request).block();
    }


}
