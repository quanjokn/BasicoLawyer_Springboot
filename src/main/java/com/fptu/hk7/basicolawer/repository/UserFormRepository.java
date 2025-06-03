package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.UserForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserFormRepository extends JpaRepository<UserForm, UUID> {
    List<UserForm> findByUserId(UUID userId);
}
