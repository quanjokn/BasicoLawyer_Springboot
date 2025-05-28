package com.fptu.hk7.basicolawer.pojo;

import com.fptu.hk7.basicolawer.enums.PaymentMethodEnum;
import com.fptu.hk7.basicolawer.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum method;

    private Float amount;
    private PaymentStatusEnum status;

    @OneToOne(mappedBy = "payment")
    private Request request;

    @OneToOne(mappedBy = "payment")
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;
}
