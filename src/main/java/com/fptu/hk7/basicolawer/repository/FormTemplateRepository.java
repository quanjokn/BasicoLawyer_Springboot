package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.FormTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormTemplateRepository extends JpaRepository<FormTemplate, UUID> {
}
