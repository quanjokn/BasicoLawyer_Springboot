package com.fptu.hk7.basicolawer.pojo;

import com.fptu.hk7.basicolawer.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private LocalDate assignedAt;

    private String linkCall;

    private String status;

    private String channel;

    private String note;

    private String lawyerFullName;

    private UUID lawerUUID;

    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum paymentStatus;

    private Integer slot;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;


}
