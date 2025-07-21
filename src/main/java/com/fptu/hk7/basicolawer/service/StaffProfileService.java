package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.ProfileRequest;
import com.fptu.hk7.basicolawer.dto.Request.StaffProfileWithSlot;
import com.fptu.hk7.basicolawer.pojo.StaffProfile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StaffProfileService {
    List<StaffProfile> findAll();
    Optional<StaffProfile> findById(UUID id);
    StaffProfile save(StaffProfile staffProfile);
    StaffProfile update(UUID id, StaffProfile staffProfile);
    void deleteById(UUID id);
    List<StaffProfileWithSlot> getStaffProfilesWithUnavailableSlots(LocalDate dateBooking);
    StaffProfile updateProfile(UUID id, ProfileRequest profileRequest);
}
