package com.upc.leadyourway.service.impl;

import com.upc.leadyourway.exception.ValidationException;
import com.upc.leadyourway.model.Bicycle;
import com.upc.leadyourway.model.Rent;
import com.upc.leadyourway.repository.BicycleRepository;
import com.upc.leadyourway.repository.RentRepository;
import com.upc.leadyourway.repository.UserRepository;
import com.upc.leadyourway.service.RentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentServiceImpl implements RentService {
    RentRepository rentRepository;
    UserRepository userRepository;
    BicycleRepository bicycleRepository;

    public RentServiceImpl(
            RentRepository rentRepository,
            UserRepository userRepository,
            BicycleRepository bicycleRepository
    ) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.bicycleRepository = bicycleRepository;
    }


    @Override
    public Rent create(Rent rent) {
        validateData(rent);
        userExists(rent.getLender().getId());
        userExists(rent.getRenter().getId());
        bicycleExists(rent.getBicycle().getId());
        return rentRepository.save(rent);
    }

    @Override
    public Rent getById(Long rent_id) {
        return rentRepository.findById(rent_id).orElse(null);
    }

    @Override
    public Rent update(Long rent_id, Rent rent) {
        validateData(rent);
        userExists(rent.getLender().getId());
        userExists(rent.getRenter().getId());
        bicycleExists(rent.getBicycle().getId());
        rent.setId(rent_id);
        return rentRepository.save(rent);
    }

    @Override
    public void delete(Long rent_id) {
        rentRepository.deleteById(rent_id);
    }

    @Override
    public List<Rent> getByLenderId(Long lender_id) {
        return rentRepository.findByLenderId(lender_id);
    }

    @Override
    public List<Rent> getByRenterId(Long renter_id) {
        return rentRepository.findByRenterId(renter_id);
    }

    @Override
    public List<Rent> getByBicycleId(Long bicycle_id) {
        return rentRepository.findByBicycleId(bicycle_id);
    }

    @Override
    public List<Rent> getByLenderIdAndRentStatus(Long lender_id, String rent_status) {
        return rentRepository.findByLenderIdAndRentStatus(lender_id, rent_status);
    }

    @Override
    public List<Rent> getByRenterIdAndRentStatus(Long renter_id, String rent_status) {
        return rentRepository.findByRenterIdAndRentStatus(renter_id, rent_status);
    }

    private void userExists(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            throw new ValidationException("User with id " + user_id + " does not exist");
        }
    }

    private void bicycleExists(Long bicycle_id) {
        if (!bicycleRepository.existsById(bicycle_id)) {
            throw new ValidationException("Bicycle with id " + bicycle_id + " does not exist");
        }
    }

    private void validateData(Rent rent){
        if (rent.getLender() == null) {
            throw new ValidationException("Lender is required");
        }
        if (rent.getRenter() == null) {
            throw new ValidationException("Renter is required");
        }
        if (rent.getBicycle() == null) {
            throw new ValidationException("Bicycle is required");
        }
        if (rent.getRentStatus() == null) {
            throw new ValidationException("Rent status is required");
        }
        if (rent.getRentStatus().isEmpty()) {
            throw new ValidationException("Rent status is required");
        }
        if (rent.getRentPrice() <= 0) {
            throw new ValidationException("Rent price must be greater than 0");
        }
        if (rent.getRentEndDate() == null) {
            throw new ValidationException("Rent date is required");
        }
        if (rent.getRentStartDate() == null) {
            throw new ValidationException("Return date is required");
        }
        if (rent.getRentStartDate().isAfter(rent.getRentEndDate())) {
            throw new ValidationException("Return end_date must be after rent start_date");
        }
    }
}
