package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.persistent.entities.Category;
import com.example.springWebMvc.repository.CategoryRepository;
import com.example.springWebMvc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    @Override
    public Page<Category> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    @Override
    public Category findById(Long catId) {
        return categoryRepository.findById(catId).orElse(null);
    }
    @Override
    public boolean updateCategory(Category category) {
        try {
            categoryRepository.save(category);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
