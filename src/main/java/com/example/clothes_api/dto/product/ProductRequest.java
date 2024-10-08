package com.example.clothes_api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest extends ProductDTO {
    private List<MultipartFile> images;
    private Set<Long> categoryIds;
    private List<Long> colorIds;
}
