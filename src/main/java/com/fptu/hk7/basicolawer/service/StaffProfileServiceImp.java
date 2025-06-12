package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import com.fptu.hk7.basicolawer.repository.StaffProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StaffProfileServiceImp implements StaffProfileService {

    private final StaffProfileRepository staffProfileRepository;

    public StaffProfileServiceImp(StaffProfileRepository staffProfileRepository) {
        this.staffProfileRepository = staffProfileRepository;
    }

    @Override
    public List<StaffProfile> findAll() {
        return staffProfileRepository.findAll();
    }

    @Override
    public Optional<StaffProfile> findById(UUID id) {
        return staffProfileRepository.findById(id);
    }

    @Override
    public StaffProfile save(StaffProfile staffProfile) {
        return staffProfileRepository.save(staffProfile);
    }

    @Override
    public StaffProfile update(UUID id, StaffProfile staffProfile) {
        if (!staffProfileRepository.existsById(id)) {
            throw new RuntimeException("StaffProfile not found");
        }
        staffProfile.setId(id);
        return staffProfileRepository.save(staffProfile);
    }

    @Override
    public void deleteById(UUID id) {
        staffProfileRepository.deleteById(id);
    }
}
