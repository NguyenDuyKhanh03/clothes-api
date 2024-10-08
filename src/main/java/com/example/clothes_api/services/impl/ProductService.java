package com.example.clothes_api.services.impl;

import com.example.clothes_api.dto.product.ProductRequest;
import com.example.clothes_api.dto.product.ProductResponse;
import com.example.clothes_api.entity.Category;
import com.example.clothes_api.entity.Color;
import com.example.clothes_api.entity.Image;
import com.example.clothes_api.entity.Product;
import com.example.clothes_api.enumEntity.Size;
import com.example.clothes_api.exception.FieldRequiredException;
import com.example.clothes_api.exception.ResourceNotFoundException;
import com.example.clothes_api.mapper.ProductMapper;
import com.example.clothes_api.repository.CategoryRepository;
import com.example.clothes_api.repository.ColorRepository;
import com.example.clothes_api.repository.ImageRepository;
import com.example.clothes_api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageService imageService;
    private final ProductMapper mapper;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;

    @Transactional
    public Product createProduct(ProductRequest request) {

        if(Objects.isNull(request.getImages())){
            throw new FieldRequiredException("Images are required");
        }
        Product savedProduct = productRepository.save(mapper.toProduct(request));
        savedProduct.setCreatedAt(new Date());
        Iterable<Long> categoryIds = request.getCategoryIds();
        Long id=categoryIds.iterator().next();
        Category category=  categoryRepository.findById(id)
                        .orElseThrow(()-> new ResourceNotFoundException("Category not found"));

        savedProduct.getCategories().add(category);
        Set<Image> images= new HashSet<>();
        for (MultipartFile file : request.getImages()) {
            Map<String,Object> cloudinaryImages=imageService.upload(file);
            Image image = new Image();
            image.setUrl(cloudinaryImages.get("secure_url").toString());
            image.setPublic_id(cloudinaryImages.get("public_id").toString());
            image.setName(savedProduct.getName());
            imageRepository.save(image);
            images.add(image);
        }
        savedProduct.setImages(images);

        for (Long colorId : request.getColorIds()) {
            Color color = colorRepository.findById(colorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Color not found"));
            savedProduct.getColors().add(color);
        }

        return productRepository.save(savedProduct);
    }

    @Transactional
    public String removeProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        for (Image image : product.getImages()) {
            String publicId = image.getPublic_id();
            imageRepository.deleteById(image.getId());
            imageService.destroyImage(publicId);
        }
        productRepository.deleteById(id);
        return "Product removed";
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapper.toProductResponse(product);
    }

    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(mapper::toProductResponse)
                .toList();
    }
}
