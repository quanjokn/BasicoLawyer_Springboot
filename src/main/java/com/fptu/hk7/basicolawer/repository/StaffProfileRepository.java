package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, UUID> {
    //    @Query("SELECT DISTINCT l FROM StaffProfile l " +
//            "JOIN l.specialties s " +
//            "WHERE s.name IN :specialties " +
//            "AND NOT EXISTS (" +
//            "   SELECT r FROM Request r " +
//            "   WHERE r.preferedLawerUUID = l.id " +
//            "   AND r.dateBooking = :dateBooking " +
//            "   AND r.slot = :slot" +
//            ")")
//    List<StaffProfile> findAvailableStaff(@Param("dateBooking") LocalDate dateBooking,
//                                          @Param("slot") int slot,
//                                          @Param("specialties") List<String> specialties);
    @Query("SELECT DISTINCT l FROM StaffProfile l " +
            "WHERE l.specialties LIKE %:specialty% " +
            "AND NOT EXISTS (" +
            "   SELECT r FROM Request r " +
            "   WHERE r.preferedLawerUUID = l.id " +
            "   AND r.dateBooking = :dateBooking " +
            "   AND r.slot = :slot" +
            ")")
    List<StaffProfile> findAvailableStaff(@Param("dateBooking") LocalDate dateBooking,
                                          @Param("slot") int slot,
                                          @Param("specialties") List<String> specialties);
}
