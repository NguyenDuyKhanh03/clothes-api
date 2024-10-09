package com.example.clothes_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderResponse {
    @JsonProperty("total_amount")
    private double totalAmount;

    private List<OrderDetailResponse> details;


}
