package com.example.clothes_api.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IIMageService {
    Map<String,Object> upload(MultipartFile multipartFile);

    void destroyImage(String publicId);

}
