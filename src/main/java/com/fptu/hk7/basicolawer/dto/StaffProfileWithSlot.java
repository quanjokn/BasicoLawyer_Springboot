package com.fptu.hk7.basicolawer.dto;

import com.fptu.hk7.basicolawer.pojo.Services;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffProfileWithSlot {
    private UUID id;
    private String name;
    private String yearExperience;
    private Float rating;
    private String avatarUrl;
    private List<String> servicesName;
    private List<Integer> unavailableSlots;
}
