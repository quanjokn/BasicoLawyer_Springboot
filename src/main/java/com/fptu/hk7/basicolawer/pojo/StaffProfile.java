package com.fptu.hk7.basicolawer.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "staff_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "user")
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
    private String specialties;
    private String description;
    private Float rating;
    private Integer cases;

    // Profile information
    @Column(name = "html_content", columnDefinition = "TEXT")
    private String htmlContent;

    @Column(name = "delta_content", columnDefinition = "TEXT")
    private String deltaContent;
    //

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "staff_profile_service",
            joinColumns = @JoinColumn(name = "staff_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Services> services;
}
