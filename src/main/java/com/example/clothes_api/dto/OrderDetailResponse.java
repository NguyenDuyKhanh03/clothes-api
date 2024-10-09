package com.example.clothes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderDetailResponse {
    private Long id;
    private String productName;
    private int quantity;
    private double price;
    private String url;
    private String size;
    private String color;
}
