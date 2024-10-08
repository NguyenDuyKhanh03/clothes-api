package com.example.clothes_api.mapper;

import com.example.clothes_api.dto.product.ProductRequest;
import com.example.clothes_api.dto.product.ProductResponse;
import com.example.clothes_api.entity.Category;
import com.example.clothes_api.entity.Color;
import com.example.clothes_api.entity.Image;
import com.example.clothes_api.entity.Product;
import com.example.clothes_api.enumEntity.Size;
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
    @Mapping(target = "colors", ignore = true)
    Product toProduct(ProductRequest request);

    ProductResponse toProductResponse(Product product);


    default List<String> colorListToStringList(List<Color> colors) {
        if(colors==null)
            return null;

        return
                colors.stream()
                        .map(Color::getName)
                        .collect(Collectors.toList());
    }

    default List<String> sizeListToStringList(Set<Size> sizes) {
        if(sizes==null)
            return null;

        return
                sizes.stream()
                        .map(Size::name)
                        .collect(Collectors.toList());
    }

    default List<Size> mapSizes(List<String> sizes) {
        if(sizes==null)
            return null;

        return
                sizes.stream()
                        .map(Size::valueOf)
                        .collect(Collectors.toList());
    }

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
