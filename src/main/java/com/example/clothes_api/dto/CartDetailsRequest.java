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
public class CartDetailsRequest {
    @JsonProperty("product_id")
    private Long productId;

    private int quantity;
    private String size;
    private String color;
}
