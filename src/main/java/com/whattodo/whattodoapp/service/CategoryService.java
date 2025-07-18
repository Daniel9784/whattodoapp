package com.whattodo.whattodoapp.service;

import com.whattodo.whattodoapp.model.Category.Category;
import com.whattodo.whattodoapp.model.Category.CategoryRepository;
import com.whattodo.whattodoapp.model.User.User;
import com.whattodo.whattodoapp.security.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<String> getCategories(CustomUserDetails userDetails) {
        return categoryRepository.findByUserId(userDetails.getUser().getId())
                .stream().map(Category::getName).toList();
    }

    public void addCategory(CustomUserDetails userDetails, String name) {
        Long userId = userDetails.getUser().getId();
        if (categoryRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("Category already exists");
        }
        Category cat = new Category();
        cat.setName(name);
        cat.setUser(userDetails.getUser());
        categoryRepository.save(cat);
    }

    public void deleteCategory(CustomUserDetails userDetails, String name) {
        Category cat = categoryRepository.findByUserIdAndName(userDetails.getUser().getId(), name);
        if (cat != null) {
            categoryRepository.delete(cat);
        }
    }

    public void renameCategory(CustomUserDetails userDetails, String oldName, String newName) {
        Category cat = categoryRepository.findByUserIdAndName(userDetails.getUser().getId(), oldName);
        if (cat != null && !categoryRepository.existsByUserIdAndName(userDetails.getUser().getId(), newName)) {
            cat.setName(newName);
            categoryRepository.save(cat);
        } else {
            throw new IllegalArgumentException("Category not found or new name exists");
        }
    }
}