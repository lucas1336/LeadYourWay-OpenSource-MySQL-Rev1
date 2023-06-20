package com.upc.leadyourway.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CardDto {
    private String cardNumber;
    private String cardType;
    private String cardCvv;
    private LocalDate cardExpirationDate;
    private Double cardAmount;
    private String cardHolder;
    private boolean cardMain;
    private Long userId;
}
