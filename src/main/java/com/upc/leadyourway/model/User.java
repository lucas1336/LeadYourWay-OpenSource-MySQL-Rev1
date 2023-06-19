package com.upc.leadyourway.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_first_name", nullable = false, length = 50)
    private String userFirstName;

    @Column(name="user_last_name", nullable = false, length = 50)
    private String userLastName;

    @Column(name="user_email", nullable = false, length = 50)
    private String userEmail;

    @Column(name="user_password", nullable = false, length = 100)
    private String userPassword;

    @Column(name="user_phone", nullable = true, length = 50)
    private String userPhone;

    @Column(name="user_birth_date", nullable = false)
    private LocalDate userBirthDate;

    @Column(name="image_data", nullable = true)
    private String imageData;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bicycle> bicycles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;
}