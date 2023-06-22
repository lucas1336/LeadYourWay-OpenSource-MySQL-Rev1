package com.upc.leadyourway.controller;

import com.upc.leadyourway.exception.ResourceNotFoundException;
import com.upc.leadyourway.exception.ValidationException;
import com.upc.leadyourway.model.Card;
import com.upc.leadyourway.model.User;
import com.upc.leadyourway.repository.CardRepository;
import com.upc.leadyourway.service.CardService;
import com.upc.leadyourway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leadyourway/v1/cards")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private UserService userService;

    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return new ResponseEntity<List<Card>>(cardRepository.findAll(), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/{cardId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/{cardId}")
    public ResponseEntity<Card> getCardById(@PathVariable(name = "cardId") Long cardId) {
        existsCardByCardId(cardId);
        return new ResponseEntity<Card>(cardService.getCardById(cardId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/user/{userId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Card>> getCardByUserId(@PathVariable(name = "userId") Long userId) {
        existsCardByUserId(userId);
        return new ResponseEntity<List<Card>>(cardRepository.findByUserId(userId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/{userId}
    // Method: POST
    @Transactional
    @PostMapping("/{userId}")
    public ResponseEntity<Card> createCard(@PathVariable(name = "userId") Long userId, @RequestBody Card card) {
        existsUserByUserId(userId);
        existsCardByCardNumber(card);
        card.setUser(userService.getUserById(userId));
        card.setCardType(card.getCardType().toLowerCase());
        validateCard(card);
        return new ResponseEntity<Card>(cardService.createCard(card), HttpStatus.CREATED);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/{cardId}
    // Method: PUT
    @Transactional
    @PutMapping("/{cardId}")
    public ResponseEntity<Card> updateCard(@PathVariable(name = "cardId") Long cardId, @RequestBody Card card) {
        existsCardByCardId(cardId);
        card.setCardType(card.getCardType().toLowerCase());
        validateCard(card);
        card.setId(cardId);
        return new ResponseEntity<Card>(cardService.updateCard(card), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/{cardId}
    // Method: DELETE
    @Transactional
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable(name = "cardId") Long cardId) {
        existsCardByCardId(cardId);
        cardService.deleteCard(cardId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    private void existsCardByCardId(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new ResourceNotFoundException("Card with id " + cardId + " not found");
        }
    }

    private void existsCardByUserId(Long userId) {
        if (!cardRepository.existsByUserId(userId)) {
            throw new ResourceNotFoundException("Card with user id " + userId + " not found");
        }
    }

    private void existsUserByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("No existe el usuario con el id: " + userId);
        }
    }

    private void existsCardByCardNumber(Card card) {
        if (cardRepository.existsByCardNumber(card.getCardNumber())) {
            throw new ValidationException("Card number already exists");
        }
    }

    private void validateCard(Card card){
        if(card.getCardAmount() < 0){
            throw new ValidationException("Card amount cannot be negative");
        }
        if(card.getCardNumber() == null || card.getCardNumber().isEmpty()){
            throw new ValidationException("Card number is required");
        }
        if(card.getCardType() == null || card.getCardType().isEmpty()){
            throw new ValidationException("Card type is required");
        }
        if(card.getCardCvv() == null || card.getCardCvv().isEmpty()){
            throw new ValidationException("Card CVV is required");
        }
        if(card.getCardHolder() == null || card.getCardHolder().isEmpty()){
            throw new ValidationException("Card holder is required");
        }
        if (card.getCardExpirationDate() == null){
            throw new ValidationException("Card expiry date is required");
        }
        if (card.getCardExpirationDate().isBefore(java.time.LocalDate.now())){
            throw new ValidationException("Card expiry date cannot be in the past");
        }
        if (card.getCardNumber().length() != 16){
            throw new ValidationException("Card number must be 16 digits");
        }
        if (card.getCardCvv().length() != 3 && card.getCardCvv().length() != 4){
            throw new ValidationException("Card CVV must be 3 digits");
        }
        if (!(card.getCardType().equals("visa")) && !(card.getCardType().equals("mastercard"))){
            throw new ValidationException("Card can only be visa or mastercard");
        }
    }
}
