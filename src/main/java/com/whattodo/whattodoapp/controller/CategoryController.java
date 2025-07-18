
package com.whattodo.whattodoapp.controller;

import com.whattodo.whattodoapp.dto.CategoryRequest;
import com.whattodo.whattodoapp.security.CustomUserDetails;
import com.whattodo.whattodoapp.service.CategoryService;
import com.whattodo.whattodoapp.service.NoteService;
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
    private final NoteService noteService;

    public CategoryController(CategoryService categoryService, NoteService noteService) {
        this.categoryService = categoryService;
        this.noteService = noteService; //
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
        try {
            categoryService.addCategory(userDetails, request.getName());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(409)
                    .body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable String name,
                                            @RequestParam(name = "deleteNotes", defaultValue = "false") boolean deleteNotes) {
        categoryService.deleteCategory(userDetails, name, deleteNotes);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{oldName}")
    public ResponseEntity<?> renameCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable String oldName,
                                            @Valid @RequestBody CategoryRequest request) {
        try {
            categoryService.renameCategory(userDetails, oldName, request.getName());

            if (request.isRenameInNotes()) {
                noteService.renameCategoryInNotes(userDetails, oldName, request.getName());
            }

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }


}