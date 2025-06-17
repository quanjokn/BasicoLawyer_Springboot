package com.fptu.hk7.basicolawer.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesDto {
    private UUID id;
    private String serviceName;
}
