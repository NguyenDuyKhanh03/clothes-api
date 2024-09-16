package com.example.clothes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AccountDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class SignUpRequest {
        private String email;

        private String full_name;

        private String password;
        private String phone_number;
    }

//    public static class SignInResponse {
//
//    }
}
