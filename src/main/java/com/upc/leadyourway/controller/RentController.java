package com.upc.leadyourway.controller;

import com.upc.leadyourway.dto.RentDto;
import com.upc.leadyourway.model.Rent;
import com.upc.leadyourway.service.RentService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leadyourway/v1")
public class RentController {
    private final RentService rentService;
    private final ModelMapper modelMapper;

    public RentController(RentService rentService, ModelMapper modelMapper) {
        this.rentService = rentService;
        this.modelMapper = modelMapper;
    }

    // URL: http://localhost:8080/api/leadyourway/v1/rents/{rentId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/rents/{rentId}")
    public ResponseEntity<Rent> getRentById(@PathVariable(name = "rentId") Long rentId) {
        return new ResponseEntity<Rent>(rentService.getById(rentId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/rents/lender/{lenderId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/rents/lender/{lenderId}")
    public ResponseEntity<List<Rent>> getRentByLenderId(@PathVariable(name = "lenderId") Long lenderId) {
        return new ResponseEntity<List<Rent>>(rentService.getByLenderId(lenderId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/rents/renter/{renterId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/rents/renter/{renterId}")
    public ResponseEntity<List<Rent>> getRentByRenterId(@PathVariable(name = "renterId") Long renterId) {
        return new ResponseEntity<List<Rent>>(rentService.getByRenterId(renterId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/rents/bicycle/{bicycleId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/rents/bicycle/{bicycleId}")
    public ResponseEntity<List<Rent>> getRentByBicycleId(@PathVariable(name = "bicycleId") Long bicycleId) {
        return new ResponseEntity<List<Rent>>(rentService.getByBicycleId(bicycleId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/rents
    // Method: POST
    @Transactional
    @PostMapping("/rents")
    public ResponseEntity<Rent> createRent(@RequestBody RentDto rentDto) {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Rent rent = modelMapper.map(rentDto, Rent.class);
        return new ResponseEntity<Rent>(rentService.create(rent), HttpStatus.CREATED);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/rents/{rentId}
    // Method: PUT
    @Transactional
    @PutMapping("/rents/{rentId}")
    public ResponseEntity<Rent> updateRent(@PathVariable(name = "rentId") Long rentId, @RequestBody Rent rent) {
        return new ResponseEntity<Rent>(rentService.update(rentId, rent), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/rents/{rentId}
    // Method: DELETE
    @Transactional
    @DeleteMapping("/rents/{rentId}")
    public ResponseEntity<Void> deleteRent(@PathVariable(name = "rentId") Long rentId) {
        rentService.delete(rentId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
