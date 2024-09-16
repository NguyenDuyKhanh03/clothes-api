package com.example.clothes_api.services;

import com.example.clothes_api.dto.AccountDTO;

public interface IAuthentication {
    void signUp(AccountDTO.SignUpRequest request);
}
