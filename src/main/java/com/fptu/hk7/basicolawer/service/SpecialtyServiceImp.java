package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Specialty;
import com.fptu.hk7.basicolawer.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpecialtyServiceImp implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyServiceImp(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public List<Specialty> findAll() {
        return specialtyRepository.findAll();
    }

    @Override
    public Optional<Specialty> findById(UUID id) {
        return specialtyRepository.findById(id);
    }

    @Override
    public Specialty save(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }

    @Override
    public Specialty update(UUID id, Specialty specialty) {
        if (!specialtyRepository.existsById(id)) {
            throw new RuntimeException("Specialty not found");
        }
        specialty.setId(id);
        return specialtyRepository.save(specialty);
    }

    @Override
    public void deleteById(UUID id) {
        specialtyRepository.deleteById(id);
    }
}
