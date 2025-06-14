package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.pojo.Services;
import com.fptu.hk7.basicolawer.service.ServicesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServicesService serviceService;

    public ServiceController(ServicesService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public List<Services> getAll() {
        return serviceService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Services> getById(@PathVariable UUID id) {
        Optional<Services> service = serviceService.findById(id);
        return service.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Services create(@RequestBody Services services) {
        return serviceService.save(services);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Services> update(@PathVariable UUID id, @RequestBody Services services) {
        try {
            return ResponseEntity.ok(serviceService.update(id, services));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        serviceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}