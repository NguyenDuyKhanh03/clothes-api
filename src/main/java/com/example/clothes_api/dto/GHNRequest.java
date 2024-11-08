package com.example.clothes_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GHNRequest {

    @JsonProperty("to_name")
    private String toName;

    @JsonProperty("from_name")
    private String fromName;

    @JsonProperty("from_phone")
    private String fromPhone;

    @JsonProperty("from_address")
    private String fromAddress;

    @JsonProperty("from_ward_name")
    private String fromWardName;

    @JsonProperty("from_district_name")
    private String fromDistrictName;

    @JsonProperty("from_province_name")
    private String fromProvinceName;

    @JsonProperty("to_phone")
    private String toPhone;

    @JsonProperty("to_address")
    private String toAddress;

    @JsonProperty("to_ward_code")
    private String toWardCode;

    @JsonProperty("to_district_id")
    private int toDistrictId;

    @JsonProperty("weight")
    private int weight;

    @JsonProperty("length")
    private int length;

    @JsonProperty("width")
    private int width;

    @JsonProperty("height")
    private int height;

    @JsonProperty("service_type_id")
    private int serviceTypeId;

    @JsonProperty("payment_type_id")
    private int paymentTypeId;

    @JsonProperty("required_note")
    private String requiredNote;

    @JsonProperty("cod_amount")
    private int codAmount;

    private List<Item> items;

}
