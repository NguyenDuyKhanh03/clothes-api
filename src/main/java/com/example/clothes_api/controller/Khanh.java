package com.example.clothes_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Khanh {

    @GetMapping("/")
    public String khanh() {
        return "Khanh";
    }
}
