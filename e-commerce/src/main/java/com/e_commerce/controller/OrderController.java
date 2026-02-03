package com.e_commerce.controller;

import com.e_commerce.dto.OrderDto;
import com.e_commerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getOrdersByUserId(null);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        OrderDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        try {
            System.out.println("Received order request: " + orderDto);
            System.out.println("First name: " + orderDto.getFirstName());
            System.out.println("Last name: " + orderDto.getLastName());
            System.out.println("Email: " + orderDto.getEmail());
            System.out.println("Total amount: " + orderDto.getTotalAmount());
            
            if (orderDto.getFirstName() == null || orderDto.getFirstName().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (orderDto.getEmail() == null || orderDto.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (orderDto.getTotalAmount() == null || orderDto.getTotalAmount() <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            OrderDto createdOrder = orderService.createOrder(orderDto);
            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        OrderDto updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
