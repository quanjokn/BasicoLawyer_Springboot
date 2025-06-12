package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(UUID id);
    Category save(Category category);
    Category update(UUID id, Category category);
    void deleteById(UUID id);
}
