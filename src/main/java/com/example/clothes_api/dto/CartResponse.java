package com.example.clothes_api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    @JsonProperty("total_price")
    private Double totalPrice;

    @JsonProperty("cart_details")
    private List<CartDetailResponse> cartDetails;
}
