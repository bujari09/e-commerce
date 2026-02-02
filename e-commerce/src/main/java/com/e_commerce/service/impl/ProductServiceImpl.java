package com.e_commerce.service.impl;

import com.e_commerce.dto.ProductDto;
import com.e_commerce.entity.Product;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.repository.ProductRepository;
import com.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(convertToDto(product));
        }
        return productDtos;
    }

    @Override
    public ProductDto getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        return convertToDto(productOptional.get());
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        try {
            System.out.println("Creating product: " + productDto.getName());
            System.out.println("Price: " + productDto.getPrice());
            System.out.println("Category ID: " + productDto.getCategoryId());
            System.out.println("Supplier ID: " + productDto.getSupplierId());
            System.out.println("City ID: " + productDto.getCityId());

            Product product = new Product();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setStockQuantity(productDto.getStockQuantity() != null ? productDto.getStockQuantity() : 0);
            product.setStatus(productDto.getStatus() != null ? productDto.getStatus().toLowerCase() : "active");
            product.setImageUrl(productDto.getImageUrl());
            product.setSupplierId(productDto.getSupplierId());
            product.setCategoryId(productDto.getCategoryId());
            product.setCityId(productDto.getCityId());
            // createdAt do të vendoset automatikisht nga @PrePersist

            Product savedProduct = productRepository.save(product);
            System.out.println("Product saved successfully with ID: " + savedProduct.getId());
            return convertToDto(savedProduct);
        } catch (Exception e) {
            System.err.println("Error creating product: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating product: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        Product product = productOptional.get();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        if (productDto.getStockQuantity() != null) {
            product.setStockQuantity(productDto.getStockQuantity());
        }
        if (productDto.getStatus() != null) {
            product.setStatus(productDto.getStatus().toLowerCase());
        }
        product.setImageUrl(productDto.getImageUrl());
        if (productDto.getSupplierId() != null) {
            product.setSupplierId(productDto.getSupplierId());
        }
        if (productDto.getCategoryId() != null) {
            product.setCategoryId(productDto.getCategoryId());
        }
        if (productDto.getCityId() != null) {
            product.setCityId(productDto.getCityId());
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(convertToDto(product));
        }
        return productDtos;
    }

    @Override
    public List<ProductDto> searchProducts(String keyword) {
        List<Product> products = productRepository.searchProducts(keyword);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(convertToDto(product));
        }
        return productDtos;
    }

    @Override
    public List<ProductDto> getAvailableProducts() {
        List<Product> products = productRepository.findByStockQuantityGreaterThan(0);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(convertToDto(product));
        }
        return productDtos;
    }

    @Override
    public List<ProductDto> getProductsByStatus(String status) {
        List<Product> products = productRepository.findByStatus(status.toLowerCase());
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(convertToDto(product));
        }
        return productDtos;
    }

    @Override
    public ProductDto updateProductStock(Long id, Integer quantity) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        Product product = productOptional.get();
        product.setStockQuantity(quantity);
        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }

    private ProductDto convertToDto(Product product) {
        try {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStockQuantity(product.getStockQuantity());
            dto.setStatus(product.getStatus());
            dto.setImageUrl(product.getImageUrl());
            dto.setSupplierId(product.getSupplierId());
            dto.setCategoryId(product.getCategoryId());
            dto.setCityId(product.getCityId());
            
            // Safe createdAt formatting
            if (product.getCreatedAt() != null) {
                try {
                    dto.setCreatedAt(product.getCreatedAt().format(formatter));
                } catch (Exception e) {
                    System.err.println("Error formatting createdAt: " + e.getMessage());
                    dto.setCreatedAt(null);
                }
            }
            
            return dto;
        } catch (Exception e) {
            System.err.println("Error converting product to DTO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to convert product to DTO", e);
        }
    }
}