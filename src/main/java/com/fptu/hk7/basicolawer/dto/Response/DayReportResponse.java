package com.fptu.hk7.basicolawer.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayReportResponse {
    private LocalDate date;
    private int dailyRevenue;
    private int newRequest;
}
