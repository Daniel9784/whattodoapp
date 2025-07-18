// src/main/java/com/whattodo/whattodoapp/controller/CategoryController.java
package com.whattodo.whattodoapp.controller;

import com.whattodo.whattodoapp.dto.CategoryRequest;
import com.whattodo.whattodoapp.security.CustomUserDetails;
import com.whattodo.whattodoapp.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<String> getCategories(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return categoryService.getCategories(userDetails);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> addCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @Valid @RequestBody CategoryRequest request) {
        categoryService.addCategory(userDetails, request.getName());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable String name) {
        categoryService.deleteCategory(userDetails, name);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{oldName}")
    public ResponseEntity<?> renameCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable String oldName,
                                            @Valid @RequestBody CategoryRequest request) {
        categoryService.renameCategory(userDetails, oldName, request.getName());
        return ResponseEntity.ok().build();
    }
}