package com.example.clothes_api.controller;

import com.example.clothes_api.dto.AccountDTO;
import com.example.clothes_api.services.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public void signUp(@RequestBody AccountDTO.SignUpRequest request) {
        authenticationService.signUp(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody AccountDTO.RegisterRequest request){
        return authenticationService.verify(request);
    }

    @GetMapping("/khanh")
    public String khanh() {
        return "Khanh";
    }
}
