package com.e_commerce.service;

import com.e_commerce.dto.OrderDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUserId(Long userId);
    OrderDto getOrderById(Long orderId);
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrderStatus(Long orderId, String status);
    void cancelOrder(Long orderId);
}