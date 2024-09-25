package com.example.clothes_api.controller;

import com.example.clothes_api.dto.AccountDTO;
import com.example.clothes_api.dto.JwtResponse;
import com.example.clothes_api.services.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<JwtResponse> login(@RequestBody AccountDTO.RegisterRequest request){
        return ResponseEntity.ok(authenticationService.verify(request));
    }

    @GetMapping("/khanh")
    public String khanh() {
        return "Khanh";
    }
}
