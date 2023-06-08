package com.upc.leadyourway.controller;

import com.upc.leadyourway.exception.ResourceNotFoundException;
import com.upc.leadyourway.exception.ValidationException;
import com.upc.leadyourway.model.Bicycle;
import com.upc.leadyourway.model.User;
import com.upc.leadyourway.repository.BicycleRepository;
import com.upc.leadyourway.service.BicycleService;
import com.upc.leadyourway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leadyourway/v1")
public class BicycleController {
    @Autowired
    private BicycleService bicycleService;
    @Autowired
    private UserService userService;

    private final BicycleRepository bicycleRepository;

    public BicycleController(BicycleRepository bicycleRepository) {
        this.bicycleRepository = bicycleRepository;
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/bicycles")
    public ResponseEntity<List<Bicycle>> getAllBicycles() {
        return new ResponseEntity<List<Bicycle>>(bicycleRepository.findAll(), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/{bicycleId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/bicycles/{bicycleId}")
    public ResponseEntity<Bicycle> getBicycleById(@PathVariable(name = "bicycleId") Long bicycleId) {
        existsBicycleByBicycleId(bicycleId);
        return new ResponseEntity<Bicycle>(bicycleService.getBicycleById(bicycleId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/filterByBicycleName
    // Method: GET
    @Transactional(readOnly = true)
    public ResponseEntity<List<Bicycle>> getBicyclesByBicycleName(@RequestParam(name = "bicycleName") String bicycleName) {
        return new ResponseEntity<List<Bicycle>>(bicycleRepository.findByBicycleName(bicycleName), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/{userId}
    // Method: POST
    @Transactional
    @PostMapping("/bicycles/{userId}")
    public ResponseEntity<Bicycle> createBicycleWithUserId(@PathVariable(name = "userId") Long userId, @RequestBody Bicycle bicycle) {
        existsUserByUserId(userId);
        bicycle.setUser(userService.getUserById(userId));
        validateBicycle(bicycle);
        return new ResponseEntity<Bicycle>(bicycleService.createBicycle(bicycle), HttpStatus.CREATED);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/{bicycleId}
    // Method: PUT
    @Transactional
    @PutMapping("/bicycles/{bicycleId}")
    public ResponseEntity<Bicycle> updateBicycleByBicycleId(@PathVariable(name = "bicycleId") Long bicycleId, @RequestBody Bicycle bicycle) {
        existsBicycleByBicycleId(bicycleId);
        bicycle.setId(bicycleId);
        validateBicycle(bicycle);
        return new ResponseEntity<Bicycle>(bicycleService.updateBicycle(bicycle), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/bicycles/{bicycleId}
    // Method: DELETE
    @Transactional
    @DeleteMapping("/bicycles/{bicycleId}")
    public ResponseEntity<String> deleteBicycleByBicycleId(@PathVariable(name = "bicycleId") Long bicycleId) {
        existsBicycleByBicycleId(bicycleId);
        bicycleService.deleteBicycle(bicycleId);
        return new ResponseEntity<String>("Bicicleta eliminada correctamente", HttpStatus.OK);
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

    private void existsUserByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("No existe el usuario con el id: " + userId);
        }
    }

    private void existsBicycleByBicycleId(Long bicycleId) {
        if (!bicycleRepository.existsById(bicycleId)) {
            throw new ResourceNotFoundException("No existe la bicicleta con el id: " + bicycleId);
        }
    }
}