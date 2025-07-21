package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicoeventdata.event.StaffProfileEvent;
import com.fptu.hk7.basicoeventdata.event.UserEvent;
import com.fptu.hk7.basicolawer.dto.Request.ProfileRequest;
import com.fptu.hk7.basicolawer.dto.Request.StaffProfileWithSlot;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.exception.ErrorCode;
import com.fptu.hk7.basicolawer.pojo.Appointment;
import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import com.fptu.hk7.basicolawer.producer.StaffProfileProducer;
import com.fptu.hk7.basicolawer.repository.AppointmentRepository;
import com.fptu.hk7.basicolawer.repository.RequestRepository;
import com.fptu.hk7.basicolawer.repository.StaffProfileRepository;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StaffProfileProducer staffProfileProducer;

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
        try {
            StaffProfile profile = staffProfileRepository.save(staffProfile);
            // sync data
            StaffProfileEvent staffProfileEvent = modelMapper.map(profile, StaffProfileEvent.class);
            UserEvent userEvent = modelMapper.map(profile.getUser(), UserEvent.class);
            staffProfileEvent.setUser(userEvent);

            staffProfileProducer.produce(staffProfileEvent, "staff-profile-create");
            return profile;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

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

    @Override
    public StaffProfile updateProfile(UUID id, ProfileRequest profileRequest) {
        StaffProfile staffProfile = staffProfileRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.INVALID_STAFF_PROFILE));
        staffProfile.setHtmlContent(profileRequest.getHtmlContent());
        return this.save(staffProfile);
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