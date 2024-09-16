package com.example.clothes_api.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.clothes_api.services.IIMageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService implements IIMageService {
    private final Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> upload(MultipartFile multipartFile) {
        try {
            return cloudinary.uploader().upload(multipartFile, ObjectUtils.asMap("resource_type","auto"));
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public void destroyImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId,ObjectUtils.asMap("invalidate", true));
        }catch (Exception e){
            return;
        }
    }
}
