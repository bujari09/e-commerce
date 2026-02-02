package com.e_commerce.service;

import com.e_commerce.dto.ProductDto;
import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    List<ProductDto> getProductsByCategoryId(Long categoryId);
    List<ProductDto> searchProducts(String keyword);
    List<ProductDto> getAvailableProducts();
    List<ProductDto> getProductsByStatus(String status);
    ProductDto updateProductStock(Long id, Integer quantity);
}