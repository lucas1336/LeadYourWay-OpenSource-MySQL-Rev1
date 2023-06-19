package com.upc.leadyourway.repository;

import com.upc.leadyourway.model.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByLenderId(Long lender_id);
    List<Rent> findByRenterId(Long renter_id);
    List<Rent> findByBicycleId(Long bicycle_id);
    List<Rent> findByLenderIdAndRentStatus(Long lender_id, String rent_status);
    List<Rent> findByRenterIdAndRentStatus(Long renter_id, String rent_status);
}
