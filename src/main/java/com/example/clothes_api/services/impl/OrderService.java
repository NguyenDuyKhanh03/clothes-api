package com.example.clothes_api.services.impl;

import com.example.clothes_api.dto.OrderResponse;
import com.example.clothes_api.entity.Account;
import com.example.clothes_api.entity.CartDetail;
import com.example.clothes_api.entity.Order;
import com.example.clothes_api.entity.OrderDetail;
import com.example.clothes_api.mapper.OrderMapper;
import com.example.clothes_api.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AccountService accountService;
    private final OrderMapper orderMapper;

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
            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartDetail.getProduct());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setSize(cartDetail.getSize());
            orderDetail.setColor(cartDetail.getColor());
            order.getDetails().add(orderDetail);
        }
        Order order1= orderRepository.save(order);
        accountService.emptyCart();
        return orderMapper.toOrderResponse(order1);
    }
}
