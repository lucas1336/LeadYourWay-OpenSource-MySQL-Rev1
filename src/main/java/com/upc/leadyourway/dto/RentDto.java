package com.upc.leadyourway.dto;

import lombok.Data;

@Data
public class RentDto {
    private String rentStartDate;
    private String rentEndDate;
    private Double rentPrice;
    private Long lenderId;
    private Long bicycleId;
}
