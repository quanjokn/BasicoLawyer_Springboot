package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Enum.Role;
import com.fptu.hk7.basicolawer.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByRole(Role role);
}
