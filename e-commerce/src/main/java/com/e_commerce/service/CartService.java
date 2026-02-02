package com.e_commerce.service;

import com.e_commerce.dto.CartDto;
import com.e_commerce.dto.CartItemDto;

public interface CartService {
    CartDto getCartByUserId(Long userId);
    CartDto addItemToCart(CartItemDto cartItemDto);
    CartDto updateCartItem(Long itemId, int quantity);
    void removeItemFromCart(Long itemId);
    void clearCart(Long userId);
}