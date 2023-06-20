package com.upc.leadyourway.controller;

import com.upc.leadyourway.model.Bicycle;
import com.upc.leadyourway.service.BicycleService;
import com.upc.leadyourway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leadyourway/v1")
public class BicycleController {
    @Autowired
    private UserService userService;

    private final BicycleService bicycleService;

    public BicycleController(BicycleService bicycleService) {
        this.bicycleService = bicycleService;
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/bicycles")
    public ResponseEntity<List<Bicycle>> getAllBicycles() {
        return new ResponseEntity<List<Bicycle>>(bicycleService.getAllBicycles(), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/{bicycleId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/bicycles/{bicycleId}")
    public ResponseEntity<Bicycle> getBicycleById(@PathVariable(name = "bicycleId") Long bicycleId) {
        return new ResponseEntity<Bicycle>(bicycleService.getBicycleById(bicycleId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/available
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/bicycles/available")
    public ResponseEntity<List<Bicycle>> getAllAvailableBicycles(
            @RequestParam(name = "start_date") LocalDate start_date,
            @RequestParam(name = "end_date") LocalDate end_date
    ) {
        return new ResponseEntity<List<Bicycle>>(bicycleService.getAllAvailableBicycles(start_date, end_date), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/{userId}
    // Method: POST
    @Transactional
    @PostMapping("/bicycles/{userId}")
    public ResponseEntity<Bicycle> createBicycleWithUserId(@PathVariable(name = "userId") Long userId, @RequestBody Bicycle bicycle) {
        return new ResponseEntity<Bicycle>(bicycleService.createBicycle(userId, bicycle), HttpStatus.CREATED);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/{bicycleId}
    // Method: PUT
    @Transactional
    @PutMapping("/bicycles/{bicycleId}")
    public ResponseEntity<Bicycle> updateBicycleByBicycleId(@PathVariable(name = "bicycleId") Long bicycleId, @RequestBody Bicycle bicycle) {
        return new ResponseEntity<Bicycle>(bicycleService.updateBicycle(bicycleId, bicycle), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/{bicycleId}
    // Method: DELETE
    @Transactional
    @DeleteMapping("/bicycles/{bicycleId}")
    public ResponseEntity<String> deleteBicycleByBicycleId(@PathVariable(name = "bicycleId") Long bicycleId) {
        return new ResponseEntity<String>("Bicicleta eliminada correctamente", HttpStatus.OK);
    }
}