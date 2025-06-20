package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.RequestDTO;
import com.fptu.hk7.basicolawer.dto.Request.UserDTO;
import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.pojo.Services;
import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RequestServiceImp implements RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    private  UserService userService;
    @Autowired
    private StaffProfileService staffProfileService;
    public RequestServiceImp(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    @Override
    public Optional<Request> findById(UUID id) {
        return requestRepository.findById(id);
    }

    @Override
    public Request save(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public Request update(UUID id, Request request) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Request not found");
        }
        request.setId(id);
        return requestRepository.save(request);
    }

    @Override
    public void deleteById(UUID id) {
        requestRepository.deleteById(id);
    }

    @Override
        public List<RequestDTO> findByUserId() {
            User user = userService.getCurrentUser();
            List<Request> requests = requestRepository.findByUserId(user.getId());
        return requests.stream()
                .filter(request -> request.getStatus() == RequestStatusEnum.PENDING) // Kiểm tra xem user có khác null không
                        .map(request -> {
            RequestDTO dto = new RequestDTO();
            dto.setId(request.getId());
            dto.setDateBooking(request.getDateBooking());
            dto.setDateRequest(request.getDateRequest());
            dto.setNote(request.getNote());
            dto.setSlot(request.getSlot());
            dto.setStatus(request.getStatus().name());

            Optional<StaffProfile> profileOpt = staffProfileService.findById(request.getPreferedLawerUUID());
            if (profileOpt.isPresent()) {
                UUID userId = profileOpt.get().getUser().getId();
                Optional<User> lawyer = userService.getUserById(userId);
                lawyer.ifPresent(user1 -> dto.setStaffName(lawyer.get().getFullname()));
            } else {
                dto.setStaffName("Không rõ");
            }
            dto.setStaffProfile(profileOpt.get());
            UserDTO dtoUser = new UserDTO();
            dtoUser.setFullName(user.getFullname());
            dtoUser.setEmail(user.getEmail());
            dtoUser.setPhone(user.getPhone());
            dto.setUser(dtoUser);

            // amount từ Payment
            if (request.getPayment() != null) {
                dto.setAmount(request.getPayment().getAmount());
            }

            // services (List<String> từ List<Services>)
            if (request.getServices() != null) {
                List<String> serviceNames = request.getServices().stream()
                        .map(Services::getName) // hoặc getTitle() nếu có
                        .collect(Collectors.toList());
                dto.setServices(serviceNames);
            }

            return dto;
        }).collect(Collectors.toList());
        }
}
