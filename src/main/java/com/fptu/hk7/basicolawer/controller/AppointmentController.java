package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Request.StatusDTO;
import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentScheduleReponse;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentLawyerListResponse;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentLawyerResponse;
import com.fptu.hk7.basicolawer.pojo.Appointment;
import com.fptu.hk7.basicolawer.service.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }



    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable UUID id) {
        Optional<Appointment> appointment = appointmentService.findById(id);
        return appointment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAppointmentLawyer() {
        List<AppointmentLawyerListResponse> appointmentList = appointmentService.findAllAppointmentLawyer();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(true)
                        .code(200)
                        .message("Get all appointment lawyer successfully")
                        .result(appointmentList)
                        .build()
        );
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse> getAllAppointmentLawyerForAdmin() {
        List<AppointmentScheduleReponse> appointmentList = appointmentService.findAllAppointmentAdmin();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(true)
                        .code(200)
                        .message("Get all appointment on admin dashboard successfully")
                        .result(appointmentList)
                        .build()
        );
    }

    @GetMapping("/lawyer")
    public ResponseEntity<ApiResponse> getScheduleLawyer() {
        List<AppointmentScheduleReponse> appointmentList = appointmentService.findScheduleLawyer();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(true)
                        .code(200)
                        .message("Get all appointment on lawyer dashboard successfully")
                        .result(appointmentList)
                        .build()
        );
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getAllAppointmentUser() {
        List<AppointmentLawyerListResponse> appointmentList = appointmentService.findAllAppointmentUser();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(true)
                        .code(200)
                        .message("Get all appointment lawyer successfully")
                        .result(appointmentList)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAppointmentLawyer(@RequestBody AppointmentLawyerResponse appointmentLawyerResponse) {
        appointmentService.createAppointmentLawyer(appointmentLawyerResponse);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(true)
                        .code(201)
                        .message("Create appointment lawyer successfully")
                        .result(null)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") UUID id,@RequestBody StatusDTO status) {
        AppointmentLawyerResponse appointment = appointmentService.updateStatusAppointment(id, status);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(true)
                        .code(200)
                        .message("Update appointment status successfully")
                        .result(appointment)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        appointmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
