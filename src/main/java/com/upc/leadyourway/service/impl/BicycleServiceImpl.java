package com.upc.leadyourway.service.impl;

import com.upc.leadyourway.exception.ResourceNotFoundException;
import com.upc.leadyourway.exception.ValidationException;
import com.upc.leadyourway.model.Availability;
import com.upc.leadyourway.model.Bicycle;
import com.upc.leadyourway.model.User;
import com.upc.leadyourway.repository.AvailabilityRepository;
import com.upc.leadyourway.repository.BicycleRepository;
import com.upc.leadyourway.service.BicycleService;
import com.upc.leadyourway.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BicycleServiceImpl implements BicycleService {
    BicycleRepository bicycleRepository;
    UserService userService;
    AvailabilityRepository availabilityRepository;

    public BicycleServiceImpl(
            BicycleRepository bicycleRepository,
            UserService userService,
            AvailabilityRepository availabilityRepository
    ) {
        this.bicycleRepository = bicycleRepository;
        this.userService = userService;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public Bicycle createBicycle(Long userId, Bicycle bicycle) {
        existsUserByUserId(userId);
        userHasCard(userId);
        bicycle.setUser(userService.getUserById(userId));
        validateBicycle(bicycle);
        return bicycleRepository.save(bicycle);
    }

    @Override
    public Bicycle getBicycleById(Long bicycle_id) {
        existsBicycleByBicycleId(bicycle_id);
        return bicycleRepository.findById(bicycle_id).orElse(null);
    }

    @Override
    public Bicycle updateBicycle(Long bicycleId, Bicycle bicycle) {
        existsBicycleByBicycleId(bicycleId);
        bicycle.setId(bicycleId);
        validateBicycle(bicycle);
        return bicycleRepository.save(bicycle);
    }

    @Override
    public void deleteBicycle(Long bicycle_id) {
        existsBicycleByBicycleId(bicycle_id);
        bicycleRepository.deleteById(bicycle_id);
    }

    @Override
    public List<Bicycle> getAllBicycles() {
        return bicycleRepository.findAll();
    }

    @Override
    public List<Bicycle> getAllAvailableBicycles(LocalDate start_date, LocalDate end_date) {
        List<Bicycle> bicycles = new ArrayList<>();
        for (Bicycle bicycle: bicycleRepository.findAll()) {
            // print bicycle id
            System.out.println("bicycle id: " + bicycle.getId());
            List<Availability> availabilities = availabilityRepository.findByBicycleId(bicycle.getId());
            boolean isAvailable = true;
            for (Availability availability: availabilities) {
                if (availability.getAvailabilityStartDate().equals(start_date) || availability.getAvailabilityEndDate().equals(start_date) ||
                        availability.getAvailabilityStartDate().equals(end_date) || availability.getAvailabilityEndDate().equals(end_date)) {
                    isAvailable = false;
                    break;
                }

                if (availability.getAvailabilityStartDate().isAfter(end_date) || availability.getAvailabilityEndDate().isBefore(start_date))
                    continue;

                if (availability.getAvailabilityStartDate().isBefore(start_date) && availability.getAvailabilityEndDate().isAfter(end_date)){
                    isAvailable = false;
                    break;
                }

                if (availability.getAvailabilityStartDate().isBefore(start_date) && availability.getAvailabilityEndDate().isBefore(end_date)){
                    isAvailable = false;
                    break;
                }

                if (availability.getAvailabilityStartDate().isAfter(start_date) && availability.getAvailabilityEndDate().isAfter(end_date)){
                    isAvailable = false;
                    break;
                }
            }
            if (isAvailable)
                bicycles.add(bicycle);
        }
        return bicycles;
    }

    private void existsBicycleByBicycleId(Long bicycleId) {
        if (!bicycleRepository.existsById(bicycleId)) {
            throw new ResourceNotFoundException("No existe la bicicleta con el id: " + bicycleId);
        }
    }

    private void existsUserByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("No existe el usuario con el id: " + userId);
        }
    }

    private void userHasCard(Long userId) {
        User user = userService.getUserById(userId);
        if (user.getCards() == null) {
            throw new ValidationException("El usuario no tiene una tarjeta asociada");
        }
    }

    private void validateBicycle(Bicycle bicycle) {
        if (bicycle.getBicycleName() == null || bicycle.getBicycleName().isEmpty()) {
            throw new ValidationException("El nombre de la bicicleta debe ser obligatorio");
        }
        if (bicycle.getBicycleName().length() > 50) {
            throw new ValidationException("El nombre de la bicicleta no debe exceder los 50 caracteres");
        }
        if (bicycle.getBicycleDescription() == null || bicycle.getBicycleDescription().isEmpty()) {
            throw new ValidationException("La descripción de la bicicleta debe ser obligatoria");
        }
        if (bicycle.getBicycleDescription().length() > 200) {
            throw new ValidationException("La descripción de la bicicleta no debe exceder los 200 caracteres");
        }
        if (bicycle.getBicyclePrice() == 0) {
            throw new ValidationException("El precio de la bicicleta debe ser obligatorio");
        }
        if (bicycle.getBicyclePrice() < 0) {
            throw new ValidationException("El precio de la bicicleta no debe ser negativo");
        }
        if (bicycle.getBicycleSize() == null || bicycle.getBicycleSize().isEmpty()) {
            throw new ValidationException("El tamaño de la bicicleta debe ser obligatorio");
        }
        if (bicycle.getUser() == null) {
            throw new ValidationException("El usuario de la bicicleta debe ser obligatorio");
        }
    }
}
