package com.e_commerce.controller;

import com.e_commerce.dto.CartDto;
import com.e_commerce.dto.CartItemDto;
import com.e_commerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable Long userId) {
        CartDto cartDto = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/add-item")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody CartItemDto cartItemDto) {
        CartDto cartDto = cartService.addItemToCart(cartItemDto);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/update-item/{itemId}")
    public ResponseEntity<CartDto> updateCartItem(
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        CartDto cartDto = cartService.updateCartItem(itemId, quantity);
        return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/remove-item/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long itemId) {
        cartService.removeItemFromCart(itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}