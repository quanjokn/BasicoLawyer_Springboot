package com.fptu.hk7.basicolawer.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;


@Data
public class ReviewDTO {
    private UUID id;
    private Float rating;
    private UUID bookingId;
    public ReviewDTO(UUID id, Float rating) {
        this.id = id;
        this.rating = rating;
    }

}
