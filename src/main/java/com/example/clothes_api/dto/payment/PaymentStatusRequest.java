package com.example.clothes_api.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusRequest {

    @JsonProperty("app_ud")
    private int appId;

    @JsonProperty("transaction_id")
    private String transactionId;

    private String mac;
}
