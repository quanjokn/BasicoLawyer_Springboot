package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import com.fptu.hk7.basicolawer.service.StaffProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff-profiles")
@SecurityRequirement(name = "bearerAuth")
public class StaffProfileController {

    private final StaffProfileService staffProfileService;

    public StaffProfileController(StaffProfileService staffProfileService) {
        this.staffProfileService = staffProfileService;
    }

    @GetMapping
    public List<StaffProfile> getAll() {
        return staffProfileService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffProfile> getById(@PathVariable UUID id) {
        Optional<StaffProfile> staffProfile = staffProfileService.findById(id);
        return staffProfile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public StaffProfile create(@RequestBody StaffProfile staffProfile) {
        return staffProfileService.save(staffProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffProfile> update(@PathVariable UUID id, @RequestBody StaffProfile staffProfile) {
        try {
            return ResponseEntity.ok(staffProfileService.update(id, staffProfile));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        staffProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
