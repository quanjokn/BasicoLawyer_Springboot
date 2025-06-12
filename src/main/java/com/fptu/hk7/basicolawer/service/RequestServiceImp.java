package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RequestServiceImp implements RequestService {

    private final RequestRepository requestRepository;

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
}
