package com.whattodo.whattodoapp.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);
    boolean existsByUserIdAndName(Long userId, String name);
    Category findByUserIdAndName(Long userId, String name);
}