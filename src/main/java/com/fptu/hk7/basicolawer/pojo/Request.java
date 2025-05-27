package com.fptu.hk7.basicolawer.pojo;

import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RequestStatusEnum status;

    private String preferedLawerUUID;

    private LocalDate date;

    private LocalTime time;

    private String note;

    private int slot;


}
