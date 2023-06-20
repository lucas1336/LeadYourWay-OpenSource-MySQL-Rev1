package com.upc.leadyourway.service.impl;

import com.upc.leadyourway.dto.AvailabilityDto;
import com.upc.leadyourway.dto.RentDto;
import com.upc.leadyourway.exception.ValidationException;
import com.upc.leadyourway.model.*;
import com.upc.leadyourway.repository.BicycleRepository;
import com.upc.leadyourway.repository.CardRepository;
import com.upc.leadyourway.repository.RentRepository;
import com.upc.leadyourway.repository.UserRepository;
import com.upc.leadyourway.service.AvailabilityService;
import com.upc.leadyourway.service.RentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class RentServiceImpl implements RentService {
    RentRepository rentRepository;
    UserRepository userRepository;
    BicycleRepository bicycleRepository;
    CardRepository cardRepository;
    AvailabilityService availabilityService;
    ModelMapper modelMapper;

    public RentServiceImpl(
            RentRepository rentRepository,
            UserRepository userRepository,
            BicycleRepository bicycleRepository,
            CardRepository cardRepository,
            AvailabilityService availabilityService,
            ModelMapper modelMapper
    ) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.bicycleRepository = bicycleRepository;
        this.cardRepository = cardRepository;
        this.availabilityService = availabilityService;
        this.modelMapper = modelMapper;
    }


    @Override
    public Rent create(RentDto rentDto) {
        validateData(rentDto);
        bicycleExists(rentDto.getBicycleId());
        cardExists(rentDto.getCardId());

        Rent rent = new Rent();

        rent.setRentStartDate(rentDto.getRentStartDate());
        rent.setRentEndDate(rentDto.getRentEndDate());
        rent.setRentPrice(rentDto.getRentPrice());
        rent.setCard(cardRepository.findById(rentDto.getCardId()).orElse(null));
        rent.setBicycle(bicycleRepository.findById(rentDto.getBicycleId()).orElse(null));

        bicycleAvailable(rent, rent.getRentStartDate(), rent.getRentEndDate());
        cardPayment(rentDto);
        return rentRepository.save(rent);
    }

    @Override
    public Rent getById(Long rent_id) {
        return rentRepository.findById(rent_id).orElse(null);
    }

    @Override
    public void delete(Long rent_id) {
        rentRepository.deleteById(rent_id);
    }

    @Override
    public List<Rent> getByBicycleId(Long bicycle_id) {
        return rentRepository.findByBicycleId(bicycle_id);
    }

    private void userExists(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            throw new ValidationException("User with id " + user_id + " does not exist");
        }
    }

    private void bicycleExists(Long bicycle_id) {
        if (!(bicycleRepository.existsById(bicycle_id))) {
            throw new ValidationException("Bicycle with id " + bicycle_id + " does not exist");
        }
        userExists(bicycleRepository.findById(bicycle_id).orElse(null).getUser().getId());
    }

    private void cardExists(Long card_id) {
        if (!cardRepository.existsById(card_id)) {
            throw new ValidationException("Card with id " + card_id + " does not exist");
        }
        userExists(cardRepository.findById(card_id).orElse(null).getUser().getId());
    }

    private void cardPayment(RentDto rent) {
        Bicycle bicycle = bicycleRepository.findById(rent.getBicycleId()).orElse(null);
        Long cardLenderId = cardRepository.findByUserIdAndCardMain(bicycle.getUser().getId(), true).getId();

        if (cardRepository.findById(rent.getCardId()).orElse(null).getCardAmount() < rent.getRentPrice()) {
            throw new ValidationException("Card with id " + rent.getCardId() + " does not have enough money");
        }
        cardRepository.findById(rent.getCardId()).orElse(null).setCardAmount(
                cardRepository.findById(rent.getCardId()).orElse(null).getCardAmount() - rent.getRentPrice()
        );
        cardRepository.save(Objects.requireNonNull(cardRepository.findById(rent.getCardId()).orElse(null)));
        cardRepository.findById(cardLenderId).orElse(null).setCardAmount(
                cardRepository.findById(cardLenderId).orElse(null).getCardAmount() + rent.getRentPrice()
        );
        cardRepository.save(Objects.requireNonNull(cardRepository.findById(cardLenderId).orElse(null)));
    }

    private void bicycleAvailable(Rent rent, LocalDate rent_start_date, LocalDate rent_end_date) {
        List<Availability> availabilityList = availabilityService.getByBicycleIdAndAvailabilityType(rent.getBicycle().getId(), false);

        for (Availability availability : availabilityList) {
            if (availability.getAvailabilityEndDate().isBefore(rent_start_date) || availability.getAvailabilityStartDate().isAfter(rent_end_date)) {
                continue;
            }

            if (availability.getAvailabilityStartDate().isBefore(rent_start_date) && availability.getAvailabilityEndDate().isAfter(rent_end_date)) {
                throw new ValidationException("The bicycle with id " + rent.getBicycle().getId() + " is not available for the requested rental period. It is already rented out.");
            }

            if (availability.getAvailabilityStartDate().isBefore(rent_start_date) && availability.getAvailabilityEndDate().isBefore(rent_end_date)) {
                throw new ValidationException("Bicycle with id " + rent.getBicycle().getId() + " is not available for the requested rental period.");
            }

            if (availability.getAvailabilityStartDate().isAfter(rent_start_date) && availability.getAvailabilityEndDate().isAfter(rent_end_date)) {
                throw new ValidationException("Bicycle with id " + rent.getBicycle().getId() + " is not available for the requested rental period.");
            }

            if (availability.getAvailabilityStartDate().equals(rent_start_date) || availability.getAvailabilityEndDate().equals(rent_start_date) ||
                    availability.getAvailabilityStartDate().equals(rent_end_date) || availability.getAvailabilityEndDate().equals(rent_end_date)) {
                throw new ValidationException("Bicycle with id " + rent.getBicycle().getId() + " is not available for the requested rental period.");
            }
        }

        AvailabilityDto availability = new AvailabilityDto();
        availability.setAvailabilityStartDate(rent_start_date);
        availability.setAvailabilityEndDate(rent_end_date);
        availability.setBicycleId(rent.getBicycle().getId());
        availabilityService.create(availability);
    }

    private void validateData(RentDto rent){
        if (rent.getBicycleId() == null) {
            throw new ValidationException("Bicycle is required");
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
        if (rent.getRentEndDate().equals(rent.getRentStartDate())) {
            throw new ValidationException("Return end_date must be after rent start_date");
        }
        if (rent.getRentStartDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Rent start_date must be after today");
        }
    }
}
