package com.upc.leadyourway.repository;

import com.upc.leadyourway.model.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BicycleRepository extends JpaRepository<Bicycle, Long> {
}
