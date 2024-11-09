package com.example.clothes_api.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZaloPaymentDto {

    @JsonProperty("app_user")
    private String appUser;

    @JsonProperty("app_id")
    private int appId;

    @JsonProperty("app_trans_id")
    private String appTransId;

    @JsonProperty("app_time")
    private Long appTime;

    private int amount;
    private String description;

    private String item;

    @JsonProperty("embed_data")
    private String embedData;

    private String mac;

}


