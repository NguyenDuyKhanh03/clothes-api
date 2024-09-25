package com.example.clothes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private String apiPath;
    private String errorMessage;
    private HttpStatus errorCode;
    private LocalDateTime errorTimes;


}
