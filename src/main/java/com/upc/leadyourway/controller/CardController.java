package com.upc.leadyourway.controller;

import com.upc.leadyourway.model.Card;
import com.upc.leadyourway.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/leadyourway/v1/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/{cardId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/{cardId}")
    public ResponseEntity<Card> getCardById(@PathVariable(name = "cardId") Long cardId) {
        return new ResponseEntity<Card>(cardService.getCardById(cardId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/user/{userId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Card>> getCardByUserId(@PathVariable(name = "userId") Long userId) {
        return new ResponseEntity<List<Card>>(cardService.getCardByUserId(userId), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/{userId}
    // Method: POST
    @Transactional
    @PostMapping("/{userId}")
    public ResponseEntity<Card> createCard(@PathVariable(name = "userId") Long userId, @RequestBody Card card) {
        return new ResponseEntity<Card>(cardService.createCard(userId, card), HttpStatus.CREATED);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/{cardId}
    // Method: PUT
    @Transactional
    @PutMapping("/{cardId}")
    public ResponseEntity<Card> updateCard(@PathVariable(name = "cardId") Long cardId, @RequestBody Card card) {
        card.setCardType(card.getCardType().toLowerCase());
        card.setId(cardId);
        return new ResponseEntity<Card>(cardService.updateCard(card), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/{cardId}/main
    // Method: PATCH
    @Transactional
    @PatchMapping("/cards/{cardId}/main")
    public ResponseEntity<Card> updateCardMain(@PathVariable(name = "cardId") Long cardId, @RequestBody boolean cardMain) {
        return new ResponseEntity<Card>(cardService.updateCardMain(cardId, cardMain), HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/leadyourway/v1/cards/{cardId}
    // Method: DELETE
    @Transactional
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable(name = "cardId") Long cardId) {
        cardService.deleteCard(cardId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
