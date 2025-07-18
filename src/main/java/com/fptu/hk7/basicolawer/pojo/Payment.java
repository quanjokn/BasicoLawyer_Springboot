package com.fptu.hk7.basicolawer.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fptu.hk7.basicolawer.enums.PaymentMethodEnum;
import com.fptu.hk7.basicolawer.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
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

    private int amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum status;

    private LocalDate createdAt;

    @OneToOne(mappedBy = "payment")
    @JsonBackReference
    private Request request;

}
