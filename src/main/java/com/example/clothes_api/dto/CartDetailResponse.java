package com.example.clothes_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailResponse {

    private Long productId;

    private String productName;
    private int quantity;
    private double price;
    private String url;
    private String size;
    private String color;
}
