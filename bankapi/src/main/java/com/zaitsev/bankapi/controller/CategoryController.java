package com.zaitsev.bankapi.controller;

import com.zaitsev.bankapi.entity.Category;
import com.zaitsev.bankapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    // создать категорию
    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    // получить все категории
    @Cacheable("categories")
    @GetMapping
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}