package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Request.RequestDTO;
import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.service.RequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@SecurityRequirement(name = "bearerAuth")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<Request> getAll() {
        return requestService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> getById(@PathVariable("id") UUID id) {
        Optional<Request> request = requestService.findById(id);
        return request.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Request create(@RequestBody Request request) {
        return requestService.save(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Request> update(@PathVariable("id") UUID id, @RequestBody Request request) {
        try {
            return ResponseEntity.ok(requestService.update(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        requestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<RequestDTO>>> getRequestsByUserId() {
        List<RequestDTO> requestDTO = requestService.findByUserId();
        return ResponseEntity.ok(
                ApiResponse.<List<RequestDTO>>builder() // 👈 ép rõ kiểu generic
                        .status(true)
                        .code(200)
                        .message("Get all request lawyer successfully")
                        .result(requestDTO)
                        .build()
        );
    }

}
