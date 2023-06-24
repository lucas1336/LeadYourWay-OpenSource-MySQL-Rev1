package com.upc.leadyourway.controller;

import com.upc.leadyourway.model.Availability;
import com.upc.leadyourway.service.AvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leadyourway/v1")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // URL: http://localhost:8080/api/leadyourway/v1/availabilities/{availabilityId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/availabilities/{availabilityId}")
    public ResponseEntity<Availability> getAvailabilityById(@PathVariable(name = "availabilityId") Long availabilityId) {
        return new ResponseEntity<Availability>(availabilityService.getById(availabilityId), HttpStatus.OK);
    }
}
