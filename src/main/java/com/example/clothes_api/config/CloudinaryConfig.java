package com.example.clothes_api.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> cloudinary = new HashMap<>();
        cloudinary.put("cloud_name", cloudName);
        cloudinary.put("api_key", apiKey);
        cloudinary.put("api_secret", apiSecret);
        cloudinary.put("secure",true);
        return new Cloudinary(cloudinary);

    }
}
