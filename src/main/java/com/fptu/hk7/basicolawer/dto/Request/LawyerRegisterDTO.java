package com.fptu.hk7.basicolawer.dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LawyerRegisterDTO {
    private RegisterDTO registerDTO;
    @NotNull(message = "year experience be not null")
    private String yearExperience;
    @NotNull(message = "licenseNumber be not null")
    private String licenseNumber;
    @NotNull(message = "bio be not null")
    private String bio;
    private boolean verified = true;
    private String avatarUrl;
    @NotNull(message = "specialties be not null")
    private String specialties;
    @NotNull(message = "description be not null")
    private String description;
    private Float rating = (float) 0;
    @Min(value = 1, message = "cases must be greater than 1")
    private Integer cases;
}
