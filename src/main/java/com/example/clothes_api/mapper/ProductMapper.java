package com.example.clothes_api.mapper;

import com.example.clothes_api.dto.product.ProductRequest;
import com.example.clothes_api.dto.product.ProductResponse;
import com.example.clothes_api.entity.Category;
import com.example.clothes_api.entity.Image;
import com.example.clothes_api.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "soldQuantity", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "categoryIds", source = "categories")
    ProductResponse toProductResponse(Product product);



    default List<String> mapImagesToUrls(Set<Image> images) {
        if(images==null)
            return null;
        return
                images.stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList());
    }

    default Set<Long> mapCategoriesToIds(Set<Category> categories) {
        if(categories==null)
            return null;
        return
                categories.stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet());
    }
}
