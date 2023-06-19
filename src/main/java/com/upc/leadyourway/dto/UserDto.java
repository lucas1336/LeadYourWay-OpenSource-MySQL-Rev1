package com.upc.leadyourway.dto;

import lombok.Data;

@Data
public class UserDto {
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userBirthDate;
    private String imageData;
}
