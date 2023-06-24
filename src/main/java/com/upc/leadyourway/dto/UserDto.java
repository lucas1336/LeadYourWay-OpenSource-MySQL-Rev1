package com.upc.leadyourway.dto;

import com.upc.leadyourway.model.Bicycle;
import com.upc.leadyourway.model.Card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPhone;
    private LocalDate userBirthDate;
    private String imageData;
    private List<Bicycle> bicycles;
    private List<Card> cards;

}
