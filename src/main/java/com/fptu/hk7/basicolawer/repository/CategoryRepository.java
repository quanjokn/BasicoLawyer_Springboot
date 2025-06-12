package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
