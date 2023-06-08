package com.upc.leadyourway.repository;

import com.upc.leadyourway.model.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BicycleRepository extends JpaRepository<Bicycle, Long> {
    boolean existsByBicycleName(String bicycle_name);
    List<Bicycle> findByBicycleName(String bicycle_name);
    List<Bicycle> findByUserId(Long user_id);
}
