package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.enums.Role;
import com.fptu.hk7.basicolawer.pojo.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByRole(Role role);
    Optional<User> findById(UUID id);

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email);
}
