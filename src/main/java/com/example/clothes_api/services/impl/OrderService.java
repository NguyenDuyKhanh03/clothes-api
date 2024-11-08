package com.example.clothes_api.services.impl;

import com.example.clothes_api.dto.GHNRequest;
import com.example.clothes_api.dto.Item;
import com.example.clothes_api.dto.OrderResponse;
import com.example.clothes_api.entity.*;
import com.example.clothes_api.exception.FieldRequiredException;
import com.example.clothes_api.exception.ResourceNotFoundException;
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

import java.util.ArrayList;
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
            Product product= productRepository.findProductForUpdate(cartDetail.getProduct().getId())
                    .orElseThrow(()-> new RuntimeException("Product not found"));

            if(product.getQuantity()<cartDetail.getQuantity()){
                throw new FieldRequiredException("Quantity not available");
            }
            product.setQuantity(product.getQuantity()-cartDetail.getQuantity());
            productRepository.save(product);

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



        return getProvinces(user.getAddresses().getProvince())
                .flatMap(provinceId -> {
                    if(provinceId==-1){
                        return Mono.error(new ResourceNotFoundException("Province not found"));
                    }
                    return getDistricts(user.getAddresses().getDistrict(), provinceId);
                })
                .flatMap(districtId -> {
                    if(districtId==-1){
                        return Mono.error(new ResourceNotFoundException("District not found"));
                    }
                    return getWard(user.getAddresses().getWard(), districtId)
                            .flatMap(wardCode -> {
                                if (wardCode == -1) {
                                    return Mono.error(new ResourceNotFoundException("Ward not found"));
                                }
                                GHNRequest requestBody = new GHNRequest();
                                requestBody.setToName(user.getName());

                                requestBody.setFromName("KaneShop");
                                requestBody.setFromPhone("0987654321");
                                requestBody.setFromAddress("72 Thành Thái, Phường 14, Quận 10, Hồ Chí Minh, Vietnam");
                                requestBody.setFromWardName("Phường 14");
                                requestBody.setFromDistrictName("Quận 10");
                                requestBody.setFromProvinceName("HCM");

                                requestBody.setToPhone(user.getNumberPhone());
                                String address = user.getAddresses().getStreet() + ", " + user.getAddresses().getWard() + ", " + user.getAddresses().getDistrict() + ", " + user.getAddresses().getProvince();
                                requestBody.setToAddress(address);

                                requestBody.setWeight(1);
                                requestBody.setHeight(1);
                                requestBody.setLength(1);
                                requestBody.setWidth(1);


                                requestBody.setItems(new ArrayList<>());
                                for (OrderDetail orderDetail : savedOrder.getDetails()) {
                                    Item item = new Item();
                                    item.setName(orderDetail.getProduct().getName());
                                    item.setQuantity(orderDetail.getQuantity());
                                    item.setWeight(1);
                                    requestBody.getItems().add(item);
                                }

                                requestBody.setToDistrictId(districtId);
                                requestBody.setToWardCode(String.valueOf(wardCode));

                                requestBody.setServiceTypeId(2);
                                requestBody.setPaymentTypeId(2);
                                requestBody.setRequiredNote("KHONGCHOXEMHANG");
                                requestBody.setCodAmount((int)savedOrder.getTotalAmount());
                                String json="";
                                try {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    json = objectMapper.writeValueAsString(requestBody);
                                    System.out.println(json);
                                } catch (Exception e) {
                                    throw new RuntimeException("Error while converting to json");
                                }
                                return webClient.post()
                                        .uri("/v2/shipping-order/create")
                                        .header("token", token)
                                        .header("ShopId", shopId)
                                        .header("Content-Type", "application/json")
                                        .bodyValue(json)
                                        .retrieve()
                                        .bodyToMono(String.class)
                                        .flatMap(response -> {

                                            Double fee = extractFee(response);
                                            String status = extractStatus(response);
                                            savedOrder.setShippingFee(fee);
                                            savedOrder.setStatus(status);
                                            Order updatedOrder = orderRepository.save(savedOrder);
                                            return Mono.just(orderMapper.toOrderResponse(updatedOrder));
                                        });
                            });
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

    private Mono<Integer> getProvinces(String provinceName){
        return webClient.get()
                .uri("/master-data/province")
                .header("token", token)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response->{
                    return Mono.just(extractProvince(response,provinceName));
                });
    }

    private Integer extractProvince(String response,String provinceName){
        try{
            ObjectMapper mapper=new ObjectMapper();
            JsonNode root=mapper.readTree(response);
            JsonNode data=root.path("data");
            for (JsonNode province : data) {
                JsonNode nameExtension=province.path("NameExtension");
                for(JsonNode name:nameExtension){
                    if(name.asText().equalsIgnoreCase(provinceName)){
                        return province.path("ProvinceID").asInt();
                    }
                }
            }
            return -1;
        }catch (Exception e){
            return -1;
        }

    }

    private Mono<Integer> getDistricts(String districtName,int provinceId){
        return webClient.get()
                .uri("/master-data/district?province_id="+provinceId)
                .header("token", token)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response->{
                    return Mono.just(extractDistrict(response,districtName));
                });
    }

    private Integer extractDistrict(String response, String districtName) {
        try{
            ObjectMapper mapper=new ObjectMapper();
            JsonNode root=mapper.readTree(response);
            JsonNode data=root.path("data");
            for (JsonNode province : data) {
                JsonNode nameExtension=province.path("NameExtension");
                for(JsonNode name:nameExtension){
                    if(name.asText().equalsIgnoreCase(districtName)){
                        return province.path("DistrictID").asInt();
                    }
                }
            }
            return -1;
        }catch (Exception e){
            return -1;
        }
    }

    private Mono<Integer> getWard(String wardName, int districtId){
        return webClient.get()
                .uri("/master-data/ward?district_id="+districtId)
                .header("token", token)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response->{
                    return Mono.just(extractWard(response,wardName));
                });
    }

    private Integer extractWard(String response, String wardName) {
        try{
            ObjectMapper mapper=new ObjectMapper();
            JsonNode root=mapper.readTree(response);
            JsonNode data=root.path("data");
            for (JsonNode province : data) {
                JsonNode nameExtension=province.path("NameExtension");
                for(JsonNode name:nameExtension){
                    if(name.asText().equalsIgnoreCase(wardName)){
                        return province.path("WardCode").asInt();
                    }
                }
            }
            return -1;
        }catch (Exception e){
            return -1;
        }
    }
}
