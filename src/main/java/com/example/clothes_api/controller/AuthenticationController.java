package com.example.clothes_api.controller;

import com.example.clothes_api.dto.AccountDTO;
import com.example.clothes_api.dto.JwtResponse;
import com.example.clothes_api.services.impl.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
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

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody AccountDTO.RegisterRequest request){
        authenticationService.resetPassword(request.getEmail(), request.getPassword());
    }
    @GetMapping("/khanh")
    public String khanh() {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        System.out.println(vnp_CreateDate);
        return "Khanh";
    }
}
