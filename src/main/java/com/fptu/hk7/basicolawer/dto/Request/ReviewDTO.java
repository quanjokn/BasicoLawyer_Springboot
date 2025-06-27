package com.fptu.hk7.basicolawer.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private UUID id;
    private Float rating;
    private UUID bookingId;
    private String comment;


}
