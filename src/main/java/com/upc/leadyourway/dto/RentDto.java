package com.upc.leadyourway.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RentDto {
    private LocalDate rentStartDate;
    private LocalDate rentEndDate;
    private Double rentPrice;
    private Long bicycleId;
    private Long cardId;
}
