package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {
    Specialty findByName(String name);
}
