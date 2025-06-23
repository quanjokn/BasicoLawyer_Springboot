package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Request.ProfileRequest;
import com.fptu.hk7.basicolawer.dto.StaffProfileWithSlot;
import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import com.fptu.hk7.basicolawer.service.StaffProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<StaffProfile> getById(@PathVariable("id") UUID id) {
        Optional<StaffProfile> staffProfile = staffProfileService.findById(id);
        return staffProfile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public StaffProfile create(@RequestBody StaffProfile staffProfile) {
        return staffProfileService.save(staffProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffProfile> update(@PathVariable("id") String id, @RequestBody StaffProfile staffProfile) {
        try {
            return ResponseEntity.ok(staffProfileService.update(UUID.fromString(id), staffProfile));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<StaffProfile> updateProfile(@PathVariable("id") String id, @Validated @RequestBody ProfileRequest profileRequest) {
        try {
            return ResponseEntity.ok(staffProfileService.updateProfile(UUID.fromString(id), profileRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        staffProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-slots")
    public List<StaffProfileWithSlot> getLawyersWithSlots(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return staffProfileService.getStaffProfilesWithUnavailableSlots(date);
    }
}
