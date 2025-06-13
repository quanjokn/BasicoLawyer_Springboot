package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Service, UUID> {
}
