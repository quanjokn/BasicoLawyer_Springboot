package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.StaffProfileWithSlot;
import com.fptu.hk7.basicolawer.pojo.Appointment;
import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import com.fptu.hk7.basicolawer.repository.AppointmentRepository;
import com.fptu.hk7.basicolawer.repository.RequestRepository;
import com.fptu.hk7.basicolawer.repository.StaffProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class StaffProfileServiceImp implements StaffProfileService {

    private final StaffProfileRepository staffProfileRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RequestRepository requestRepository;
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

    @Override
    public List<StaffProfileWithSlot> getStaffProfilesWithUnavailableSlots(LocalDate dateBooking) {
        List<StaffProfile> allStaff = staffProfileRepository.findAll();

        // Lấy tất cả appointment trong ngày được chọn
        List<Appointment> allRequests = appointmentRepository.findByAssignedAt(dateBooking);

        Map<UUID, List<Integer>> slotsByStaff = allRequests.stream()
                .filter(r -> r.getLawerUUID() != null)
                .collect(Collectors.groupingBy(
                        Appointment::getLawerUUID,
                        Collectors.mapping(Appointment::getSlot, Collectors.toList())
                ));

        return allStaff.stream().map(staff -> {
            UUID id = staff.getId();
            List<String> serviceNames = staff.getServices() != null
                    ? staff.getServices().stream()
                    .map(service -> service.getName())
                    .collect(Collectors.toList())
                    : Collections.emptyList();
            List<Integer> slots = slotsByStaff.getOrDefault(id, new ArrayList<>());
            return new StaffProfileWithSlot(
                    id,
                    staff.getUser().getFullname(),
                    staff.getYearExperience(),
                    staff.getRating(),
                    staff.getAvatarUrl(),
                    serviceNames,
                    slots
            );
        }).collect(Collectors.toList());
    }
}

//@Override
//public List<StaffProfileWithSlot> getStaffProfilesWithUnavailableSlots(LocalDate dateBooking) {
//    List<StaffProfile> allStaff = staffProfileRepository.findAll();
//
//    // Lấy tất cả request trong ngày được chọn
//    List<Request> allRequests = requestRepository.findByDateBooking(dateBooking);
//
//    Map<UUID, List<Integer>> slotsByStaff = allRequests.stream()
//            .filter(r -> r.getPreferedLawerUUID() != null)
//            .collect(Collectors.groupingBy(
//                    Request::getPreferedLawerUUID,
//                    Collectors.mapping(Request::getSlot, Collectors.toList())
//            ));
//
//    return allStaff.stream().map(staff -> {
//        UUID id = staff.getId();
//        List<String> serviceNames = staff.getServices() != null
//                ? staff.getServices().stream()
//                .map(service -> service.getName())
//                .collect(Collectors.toList())
//                : Collections.emptyList();
//        List<Integer> slots = slotsByStaff.getOrDefault(id, new ArrayList<>());
//        return new StaffProfileWithSlot(
//                id,
//                staff.getUser().getFullname(),
//                staff.getYearExperience(),
//                staff.getRating(),
//                staff.getAvatarUrl(),
//                serviceNames,
//                slots
//        );
//    }).collect(Collectors.toList());
//}