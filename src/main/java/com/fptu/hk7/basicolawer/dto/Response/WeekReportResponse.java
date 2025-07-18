package com.fptu.hk7.basicolawer.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekReportResponse {
    private int totalWeekRevenue;
    private List<DayReportResponse> dailyReports;
}
