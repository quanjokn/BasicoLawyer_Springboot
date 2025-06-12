package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Specialty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecialtyService {
    List<Specialty> findAll();
    Optional<Specialty> findById(UUID id);
    Specialty save(Specialty specialty);
    Specialty update(UUID id, Specialty specialty);
    void deleteById(UUID id);
}
