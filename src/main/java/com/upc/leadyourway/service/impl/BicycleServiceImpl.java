package com.upc.leadyourway.service.impl;

import com.upc.leadyourway.model.Bicycle;
import com.upc.leadyourway.repository.BicycleRepository;
import com.upc.leadyourway.service.BicycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BicycleServiceImpl implements BicycleService {
    @Autowired
    private BicycleRepository bicycleRepository;

    @Override
    public Bicycle createBicycle(Bicycle bicycle) {
        return bicycleRepository.save(bicycle);
    }

    @Override
    public Bicycle getBicycleById(Long bicycle_id) {
        return bicycleRepository.findById(bicycle_id).orElse(null);
    }

    @Override
    public Bicycle updateBicycle(Bicycle bicycle) {
        return bicycleRepository.save(bicycle);
    }

    @Override
    public void deleteBicycle(Long bicycle_id) {
        bicycleRepository.deleteById(bicycle_id);
    }
}
