package com.example.clothes_api.services.impl;

import com.example.clothes_api.dto.CartDetailResponse;
import com.example.clothes_api.dto.CartDetailsRequest;
import com.example.clothes_api.dto.CartResponse;
import com.example.clothes_api.entity.Account;
import com.example.clothes_api.entity.Cart;
import com.example.clothes_api.entity.CartDetail;
import com.example.clothes_api.entity.Product;
import com.example.clothes_api.mapper.CartMapper;
import com.example.clothes_api.repository.CartRepository;
import com.example.clothes_api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final AccountService accountService;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Transactional
    public CartResponse addProductToCart(CartDetailsRequest request) {
        Account user= accountService.getAccount()
                .orElseThrow(()-> new RuntimeException("User not found"));
        Cart cart=user.getCart();
        if(Objects.nonNull(cart) && Objects.nonNull(cart.getCartDetails()) && !cart.getCartDetails().isEmpty()){
            Optional<CartDetail> cartDetail= cart.getCartDetails().stream()
                    .filter(cd-> cd.getProduct().getId().equals(request.getProductId()))
                    .findAny();

            if(cartDetail.isPresent()){
                if(cartDetail.get().getProduct().getQuantity()< cartDetail.get().getQuantity()+request.getQuantity()){
                    throw new RuntimeException("Quantity not available");
                }
                cartDetail.get().setQuantity(cartDetail.get().getQuantity()+request.getQuantity());
                cart.setTotalPrice(calculateTotalPrice(cart));
                Cart cart1= cartRepository.save(cart);
                return cartMapper.toCartResponse(cart1);
            }
        }

        if(Objects.isNull(cart)){
            cart= createCart(user);
        }
        Product product=productRepository.findById(request.getProductId())
                .orElseThrow(()-> new RuntimeException("Product not found"));
        CartDetail cartDetail= new CartDetail();
        cartDetail.setCart(cart);
        cartDetail.setProduct(product);
        cartDetail.setQuantity(request.getQuantity());
        cartDetail.setSize(request.getSize());
        cartDetail.setColor(request.getColor());

        if(Objects.isNull(cart.getCartDetails())){
            cart.setCartDetails(new ArrayList<>());
        }
        cart.getCartDetails().add(cartDetail);
        cart.setTotalPrice(calculateTotalPrice(cart));
        Cart cart1= cartRepository.save(cart);
        return cartMapper.toCartResponse(cart1);
    }

    @Transactional
    public CartResponse removeProductFromCart(Long productId, int quantity) {
        Account user= accountService.getAccount()
                .orElseThrow(()-> new RuntimeException("User not found"));
        Cart cart=user.getCart();
        if(Objects.isNull(cart) || Objects.isNull(cart.getCartDetails()) || cart.getCartDetails().isEmpty()){
            throw new RuntimeException("Cart is empty");
        }
        CartDetail cartDetail= cart.getCartDetails().stream()
                .filter(cd-> cd.getProduct().getId().equals(productId))
                .findAny()
                .orElseThrow(()-> new RuntimeException("Cart detail not found"));

        if(cartDetail.getQuantity()-quantity<=1){
            List<CartDetail> cartDetails= cart.getCartDetails();
            cartDetails.remove(cartDetail);
            cart.setTotalPrice(calculateTotalPrice(cart));
            return cartMapper.toCartResponse(cart);
        }
        cartDetail.setQuantity(cartDetail.getQuantity()-quantity);
        cart.setTotalPrice(calculateTotalPrice(cart));
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse incrementCartItem(Long productId, int quantity) {
        Account user= accountService.getAccount()
                .orElseThrow(()-> new RuntimeException("User not found"));
        Cart cart=user.getCart();
        if(Objects.isNull(cart) || Objects.isNull(cart.getCartDetails()) || cart.getCartDetails().isEmpty()){
            throw new RuntimeException("Cart is empty");
        }
        CartDetail cartDetail= cart.getCartDetails().stream()
                .filter(cd-> cd.getProduct().getId().equals(productId))
                .findAny()
                .orElseThrow(()-> new RuntimeException("Cart detail not found"));

        if(cartDetail.getQuantity()+quantity>cartDetail.getProduct().getQuantity()){
            throw new RuntimeException("Quantity not available");
        }
        cartDetail.setQuantity(cartDetail.getQuantity()+quantity);
        cart.setTotalPrice(calculateTotalPrice(cart));
        Cart cart1= cartRepository.save(cart);
        return cartMapper.toCartResponse(cart1);
    }

    public Double calculateTotalPrice(Cart cart){
        double totalPrice= 0L;
        for (CartDetail cartDetail : cart.getCartDetails()) {
            totalPrice+= (long) (cartDetail.getProduct().getPrice()*cartDetail.getQuantity());
        }

        return totalPrice;
    }


    private Cart createCart(Account user){
        Cart cart= new Cart();
        cart.setUser(user);
        return cart;
    }

    public void emptyCart() {
        Account user= accountService.getAccount().get();
        user.setCart(null);
        accountService.saveAccount(user);
    }

    public List<CartDetailResponse> getCartDetails() {
        Account user = accountService.getAccount()
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = user.getCart();
        if (Objects.isNull(cart) || Objects.isNull(cart.getCartDetails()) || cart.getCartDetails().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        return cartMapper.toCartDetailToCartDetailResponse(cart.getCartDetails());
    }
}
