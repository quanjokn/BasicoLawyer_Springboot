package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
