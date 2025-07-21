package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.pojo.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    // Các phương thức truy vấn tùy chỉnh nếu cần
    Optional<Request> findByTxnRef(String txnRef);

    List<Request> findByDateBooking( LocalDate dateBooking);

    List<Request> findByUserId(UUID userId);

    List<Request> findByServices(Services service);
}
