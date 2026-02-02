package com.e_commerce.config;

import com.e_commerce.entity.Category;
import com.e_commerce.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initCategories(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                // Krijo kategori bazë
                Category electronics = new Category();
                electronics.setName("Electronics");
                electronics.setDescription("Electronic devices and gadgets");

                Category clothing = new Category();
                clothing.setName("Clothing");
                clothing.setDescription("Clothes and accessories");

                Category books = new Category();
                books.setName("Books");
                books.setDescription("Books of all genres");

                categoryRepository.save(electronics);
                categoryRepository.save(clothing);
                categoryRepository.save(books);

                System.out.println("Basic categories created!");
            }
        };
    }
}