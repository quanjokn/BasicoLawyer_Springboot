package com.fptu.hk7.basicolawer.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;


@Data
@AllArgsConstructor
public class ReviewDTO {
    private UUID id;
    private Float rating;
    private UUID bookingId;
    private String comment;


}
