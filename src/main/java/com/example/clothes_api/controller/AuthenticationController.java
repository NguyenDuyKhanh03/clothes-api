package com.example.clothes_api.controller;

import com.example.clothes_api.dto.AccountDTO;
import com.example.clothes_api.services.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/add-user")
    public void signUp(@RequestBody AccountDTO.SignUpRequest request) {
        authenticationService.signUp(request);
    }
}
