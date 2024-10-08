package com.example.clothes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailResponse {
    private Long id;
    private String productName;
    private int quantity;
    private double price;
    private String url;
    private String size;
    private String color;
}
