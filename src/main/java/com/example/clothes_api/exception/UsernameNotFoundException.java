package com.example.clothes_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UsernameNotFoundException extends org.springframework.security.core.userdetails.UsernameNotFoundException {
    public UsernameNotFoundException(String msg) {
        super(msg);
    }
}
