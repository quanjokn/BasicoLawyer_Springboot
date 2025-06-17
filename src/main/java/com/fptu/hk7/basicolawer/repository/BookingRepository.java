package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import com.fptu.hk7.basicolawer.pojo.Booking;
import com.fptu.hk7.basicolawer.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findAllByUserAndRequest_Status(User user, RequestStatusEnum status);
    List<Booking> findAllByUser(User user);
}
