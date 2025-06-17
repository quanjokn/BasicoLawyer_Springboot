package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.pojo.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Services, UUID> {
    Optional<Services> findByName(String name);

    List<Services> findAllByRequests(Request request);
}
