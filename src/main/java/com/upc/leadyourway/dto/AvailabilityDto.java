package com.upc.leadyourway.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AvailabilityDto {
    private LocalDate availabilityStartDate;
    private LocalDate availabilityEndDate;
    private Long bicycleId;
}
