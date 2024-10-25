package com.example.clothes_api.controller;

import com.example.clothes_api.dto.AccountDTO;
import com.example.clothes_api.dto.MailRequest;
import com.example.clothes_api.dto.MailResponse;
import com.example.clothes_api.services.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {
    private final EmailService emailService;

    @PostMapping("/send-email")
    public MailResponse sendEmail(@RequestBody MailRequest request){
        Map<String, Object> model=new HashMap<>();
        model.put("name", request.getName());
        model.put("location", "Ho Chi Minh City, Viet Nam");
        return emailService.sendEmail(request, model);
    }

    @GetMapping("/verify-otp")
    public String verifyOtp(@RequestBody AccountDTO.ForgotPasswordRequest request){
        return emailService.verifyOtp(request.getEmail(), request.getOtp());
    }

}
