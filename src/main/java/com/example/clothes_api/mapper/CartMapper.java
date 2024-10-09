package com.example.clothes_api.mapper;

import com.example.clothes_api.dto.CartDetailResponse;
import com.example.clothes_api.dto.CartResponse;
import com.example.clothes_api.entity.Cart;
import com.example.clothes_api.entity.CartDetail;
import com.example.clothes_api.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {


    @Mapping(target = "cartDetails", source = "cartDetails")
    @Mapping(target = "totalPrice", source = "totalPrice")
    CartResponse toCartResponse(Cart cart);


    default List<CartDetailResponse> toCartDetailToCartDetailResponse(List<CartDetail> cartDetails) {
        if(cartDetails==null)
            return null;

        List<CartDetailResponse> cartDetailResponses = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            CartDetailResponse cartDetailResponse = new CartDetailResponse();
            cartDetailResponse.setId(cartDetail.getId());
            cartDetailResponse.setColor(cartDetail.getColor());
            cartDetailResponse.setQuantity(cartDetail.getQuantity());
            cartDetailResponse.setSize(cartDetail.getSize());
            cartDetailResponse.setUrl(
                    cartDetail.getProduct().getImages()
                            .stream()
                            .findFirst()
                            .map(Image::getUrl)
                            .orElse(null)
                    );
            cartDetailResponse.setPrice(cartDetail.getProduct().getPrice());
            cartDetailResponse.setProductName(cartDetail.getProduct().getName());
            cartDetailResponses.add(cartDetailResponse);
        }
        return cartDetailResponses;
    }
}
