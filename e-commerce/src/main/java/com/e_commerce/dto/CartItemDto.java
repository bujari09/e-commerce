package com.e_commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private Long cartId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subtotal;
}