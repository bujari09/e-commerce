package com.e_commerce.service.impl;

import com.e_commerce.dto.OrderDto;
import com.e_commerce.entity.Order;
import com.e_commerce.entity.Customer;
import com.e_commerce.repository.OrderRepository;
import com.e_commerce.repository.CustomerRepository;
import com.e_commerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return convertToDto(order);
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        try {
            // Create or find customer
            Customer customer = customerRepository.findByEmail(orderDto.getEmail());
            if (customer == null) {
                customer = new Customer();
                customer.setFirstName(orderDto.getFirstName());
                customer.setLastName(orderDto.getLastName());
                customer.setAddress(orderDto.getAddress());
                customer.setPhone(orderDto.getPhone());
                customer.setEmail(orderDto.getEmail());
                customer = customerRepository.save(customer);
            }
            
            // Create order
            Order order = new Order();
            order.setUserId(customer.getId()); // Use customer ID as user_id
            order.setTotalAmount(orderDto.getTotalAmount());
            order.setStatus("pending");
            
            Order savedOrder = orderRepository.save(order);
            System.out.println("Order created successfully with ID: " + savedOrder.getId());
            
            return convertToDto(savedOrder, customer);
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setStatus("cancelled");
        orderRepository.save(order);
    }

    private OrderDto convertToDto(Order order) {
        // Find customer by user_id
        Customer customer = customerRepository.findById(order.getUserId()).orElse(null);
        return convertToDto(order, customer);
    }

    private OrderDto convertToDto(Order order, Customer customer) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        
        if (order.getOrderDate() != null) {
            dto.setOrderDate(order.getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        
        // Set customer info
        if (customer != null) {
            dto.setFirstName(customer.getFirstName());
            dto.setLastName(customer.getLastName());
            dto.setAddress(customer.getAddress());
            dto.setPhone(customer.getPhone());
            dto.setEmail(customer.getEmail());
        } else {
            // Default values if customer not found
            dto.setFirstName("Customer");
            dto.setLastName("Name");
            dto.setAddress("Delivery Address");
            dto.setPhone("Phone Number");
            dto.setEmail("customer@email.com");
        }
        
        return dto;
    }
}
