package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Services;
import com.fptu.hk7.basicolawer.repository.ServiceRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ServicesServiceImpl implements ServicesService {
    private final ServiceRepository serviceRepository;

    public ServicesServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<Services> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public Optional<Services> findById(UUID id) {
        return serviceRepository.findById(id);
    }

    @Override
    public Services save(Services services) {
        return serviceRepository.save(services);
    }

    @Override
    public Services update(UUID id, Services services) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Service not found");
        }
        services.setId(id);
        return serviceRepository.save(services);
    }

    @Override
    public void deleteById(UUID id) {
        serviceRepository.deleteById(id);
    }

    @Override
    public Optional<Services> findByName(String name) {
        Optional<Services> services = serviceRepository.findByName(name);
        return services;
    }
}
