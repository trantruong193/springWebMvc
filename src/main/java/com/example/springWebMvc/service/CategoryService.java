package com.example.springWebMvc.service;

import com.example.springWebMvc.persistent.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Page<Category> getAll(Pageable pageable);
    Category findById(Long catId);
    boolean updateCategory(Category category);
}
