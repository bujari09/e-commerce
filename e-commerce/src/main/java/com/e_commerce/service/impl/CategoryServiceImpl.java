package com.e_commerce.service.impl;

import com.e_commerce.dto.CategoryDto;
import com.e_commerce.entity.Category;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.repository.CategoryRepository;
import com.e_commerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories() {  // Kjo duhet të përputhet me interface
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();

        for (Category category : categories) {
            categoryDtos.add(convertToDto(category));
        }

        return categoryDtos;
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return convertToDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        try {
            System.out.println("Creating category: " + categoryDto.getName());
            System.out.println("Description: " + categoryDto.getDescription());
            System.out.println("Image URL: " + categoryDto.getImageUrl());

            Category category = new Category();
            category.setName(categoryDto.getName());
            category.setDescription(categoryDto.getDescription());
            category.setImageUrl(categoryDto.getImageUrl());

            Category savedCategory = categoryRepository.save(category);
            System.out.println("Category saved successfully with ID: " + savedCategory.getId());
            return convertToDto(savedCategory);
        } catch (Exception e) {
            System.err.println("Error creating category: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating category: " + e.getMessage(), e);
        }
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setImageUrl(categoryDto.getImageUrl());

        Category updatedCategory = categoryRepository.save(category);
        return convertToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setImageUrl(category.getImageUrl());
        return dto;
    }
}