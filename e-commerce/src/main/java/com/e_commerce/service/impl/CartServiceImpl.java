package com.e_commerce.service.impl;

import com.e_commerce.dto.CartDto;
import com.e_commerce.dto.CartItemDto;
import com.e_commerce.service.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Override
    public CartDto getCartByUserId(Long userId) {
        return new CartDto();
    }

    @Override
    public CartDto addItemToCart(CartItemDto cartItemDto) {
        return new CartDto();
    }

    @Override
    public CartDto updateCartItem(Long itemId, int quantity) {
        return new CartDto();
    }

    @Override
    public void removeItemFromCart(Long itemId) {

    }

    @Override
    public void clearCart(Long userId) {

    }
}