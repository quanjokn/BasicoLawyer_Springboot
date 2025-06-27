package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServicesService {
    List<Services> findAll();
    Optional<Services> findById(UUID id);
    Services save(Services services);
    Services update(UUID id, Services services);
    void deleteById(UUID id);
    Optional<Services> findByName(String name);
}
