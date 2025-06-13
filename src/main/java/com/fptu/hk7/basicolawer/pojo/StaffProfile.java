package com.fptu.hk7.basicolawer.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "staff_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffProfile {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String yearExperience;
    private String licenseNumber;
    private String bio;
    private boolean verified;
    private String avatarUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "staff_profile_specialty",
            joinColumns = @JoinColumn(name = "staff_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private List<Specialty> specialties;
}
