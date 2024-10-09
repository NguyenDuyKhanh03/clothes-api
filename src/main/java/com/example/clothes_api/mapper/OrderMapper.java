package com.example.clothes_api.mapper;

import com.example.clothes_api.dto.OrderDetailResponse;
import com.example.clothes_api.dto.OrderResponse;
import com.example.clothes_api.entity.Image;
import com.example.clothes_api.entity.Order;
import com.example.clothes_api.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "details", source = "details")
    OrderResponse toOrderResponse(Order order);


    default List<OrderDetailResponse> toOrderResponseList(List<OrderDetail> orderDetails){
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

        for (OrderDetail o:orderDetails){
            OrderDetailResponse orderDetailResponse=new OrderDetailResponse();
            orderDetailResponse.setId(o.getId());
            orderDetailResponse.setPrice(o.getProduct().getPrice());
            orderDetailResponse.setQuantity(o.getQuantity());
            orderDetailResponse.setProductName(o.getProduct().getName());
            orderDetailResponse.setSize(o.getSize());
            orderDetailResponse.setColor(o.getColor());
            orderDetailResponse.setUrl(
                    o.getProduct().getImages().stream()
                            .findFirst()
                            .map(Image::getUrl)
                            .orElse(null)
            );
            orderDetailResponses.add(orderDetailResponse);
        }
        return orderDetailResponses;
    }

}
