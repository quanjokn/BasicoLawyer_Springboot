package com.fptu.hk7.basicolawer.pojo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "specialty")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Specialty {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<StaffProfile> specialties;
}
