package com.example.clothes_api.services.impl;

import com.example.clothes_api.dto.OrderResponse;
import com.example.clothes_api.entity.*;
import com.example.clothes_api.exception.FieldRequiredException;
import com.example.clothes_api.mapper.OrderMapper;
import com.example.clothes_api.repository.OrderRepository;
import com.example.clothes_api.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AccountService accountService;
    private final OrderMapper orderMapper;
    private final WebClient webClient;
    private final ProductRepository productRepository;

    @Value("${token_shop}")
    private String token;

    @Value("${shop_id}")
    private String shopId;

    @Transactional
    public OrderResponse createOrder() {
        Account user=accountService.getAccount()
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(Objects.isNull(user.getCart()) || Objects.isNull(user.getCart().getCartDetails()) || user.getCart().getCartDetails().isEmpty()){
            throw new RuntimeException("Cart is empty");
        }

        Order order= new Order();
        order.setUser(user);
        order.setTotalAmount(user.getCart().getTotalPrice());
        order.setOrderDate(new Date());
        for (CartDetail cartDetail : user.getCart().getCartDetails()) {
            try{
                Product product= productRepository.findProductForUpdate(cartDetail.getProduct().getId())
                        .orElseThrow(()-> new RuntimeException("Product not found"));

                if(product.getQuantity()<cartDetail.getQuantity()){
                    throw new FieldRequiredException("Quantity not available");
                }

                product.setQuantity(product.getQuantity()-cartDetail.getQuantity());
                productRepository.save(product);

                OrderDetail orderDetail=new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cartDetail.getProduct());
                orderDetail.setQuantity(cartDetail.getQuantity());
                orderDetail.setSize(cartDetail.getSize());
                orderDetail.setColor(cartDetail.getColor());
                order.getDetails().add(orderDetail);
            }catch (OptimisticLockException e){
                throw new RuntimeException("Product is being updated");
            }

        }
        Order order1= orderRepository.save(order);
        accountService.emptyCart();
        return orderMapper.toOrderResponse(order1);
    }

    @Transactional
    public Mono<OrderResponse> createOrderWithGHN() {
        Account user = accountService.getAccount()
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Objects.isNull(user.getCart()) || Objects.isNull(user.getCart().getCartDetails()) || user.getCart().getCartDetails().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(user.getCart().getTotalPrice());
        order.setOrderDate(new Date());
        for (CartDetail cartDetail : user.getCart().getCartDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartDetail.getProduct());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setSize(cartDetail.getSize());
            orderDetail.setColor(cartDetail.getColor());
            order.getDetails().add(orderDetail);
        }
        Order savedOrder = orderRepository.save(order);
        accountService.emptyCart();
        orderMapper.toOrderResponse(savedOrder);
        String requestBody = """
                {
                        "payment_type_id":2,
                        "note":"Tintest 123",
                        "required_note":"KHONGCHOXEMHANG",
                        "return_phone":"0373886039",
                        "return_address":"39 NTT",
                        "from_name":"khanh",
                        "from_address": "72 Thành Thái, Phường 14, Quận 10, Hồ Chí Minh, Vietnam",
                        "from_ward_name": "Phường 14",
                        "from_district_name": "Quận 10",
                        "from_province_name": "HCM",
                        "return_district_id":3254,
                        "return_ward_code":"370519",
                        "to_name":"TinTest124",
                        "to_phone":"0987654321",
                        "to_address":"Chanh Thuan, My Trinh, Phu My, Binh Dinh, Vietnam",
                        "to_ward_code":"370513",
                        "to_district_id":3254,
                        "cod_amount":200000,
                        "weight":5,
                        "length":1,
                        "width":19,
                        "height":10,
                        "pick_station_id":1444,
                        "deliver_station_id":null,
                        "insurance_value":5000000,
                        "service_id":0,
                        "service_type_id":2,
                        "pick_shift":[
                           2
                        ],
                        "items":[
                           {
                              "name":"Áo Polo",
                              "code":"Polo123",
                              "quantity":1,
                              "price":200000,
                              "length":12,
                              "width":12,
                              "height":12,
                              "weight":1200,
                              "category":{
                                 "level1":"Áo"
                              }
                           }
                        ]
                }
            """;

        return webClient.post()
                .uri("/shipping-order/create")
                .header("token", token)
                .header("ShopId", shopId)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response->{
                    Double fee=extractFee(response);
                    String status=extractStatus(response);
                    savedOrder.setShippingFee(fee);
                    savedOrder.setStatus(status);
                    Order updatedOrder = orderRepository.save(savedOrder);
                    return Mono.just(orderMapper.toOrderResponse(updatedOrder));
                })
                .onErrorResume(e->{
                    return Mono.error(new RuntimeException("Error while creating order"));
                });
    }

    private Double extractFee(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("data").path("total_fee").asDouble();
        }catch (Exception e){
            throw new RuntimeException("Error while extracting fee");
        }
    }
    private String extractStatus(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("message").asText();
        }catch (Exception e){
            throw new RuntimeException("Loi gi do");
        }
    }
}
