package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.pojo.Specialty;
import com.fptu.hk7.basicolawer.service.SpecialtyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public List<Specialty> getAll() {
        return specialtyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> getById(@PathVariable UUID id) {
        Optional<Specialty> specialty = specialtyService.findById(id);
        return specialty.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Specialty create(@RequestBody Specialty specialty) {
        return specialtyService.save(specialty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> update(@PathVariable UUID id, @RequestBody Specialty specialty) {
        try {
            return ResponseEntity.ok(specialtyService.update(id, specialty));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        specialtyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
