package com.upc.leadyourway.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="rents")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="rent_start_date", nullable = false)
    private LocalDate rentStartDate;

    @Column(name="rent_end_date", nullable = false)
    private LocalDate rentEndDate;

    @Column(name="rent_price", nullable = false)
    private Double rentPrice;

    @Column(name="rent_status", nullable = false)
    private String rentStatus;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "lender_id", referencedColumnName = "id")
    private User lender;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "renter_id", referencedColumnName = "id")
    private User renter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bicycle_id", referencedColumnName = "id")
    private Bicycle bicycle;
}
