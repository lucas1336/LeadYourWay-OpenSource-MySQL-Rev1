package com.upc.leadyourway.service.impl;

import com.upc.leadyourway.exception.ResourceNotFoundException;
import com.upc.leadyourway.exception.ValidationException;
import com.upc.leadyourway.model.Card;
import com.upc.leadyourway.model.User;
import com.upc.leadyourway.repository.CardRepository;
import com.upc.leadyourway.repository.UserRepository;
import com.upc.leadyourway.service.CardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    CardRepository cardRepository;
    UserRepository userRepository;

    public CardServiceImpl(
            CardRepository cardRepository,
            UserRepository userRepository
    ) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Card createCard(Long userId, Card card) {
        existsUserByUserId(userId);
        existsCardByCardNumber(card);
        validateCard(card);
        card.setCardMain(firstCard(userId));
        card.setCardType(card.getCardType().toLowerCase());
        card.setUser(userRepository.findById(userId).get());
        return cardRepository.save(card);
    }

    @Override
    public Card getCardById(Long card_id) {
        existsCardByCardId(card_id);
        return cardRepository.findById(card_id).orElse(null);
    }

    @Override
    public Card updateCard(Card card) {
        existsCardByCardId(card.getId());
        validateCard(card);
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long card_id) {
        existsCardByCardId(card_id);
        cardRepository.deleteById(card_id);
    }

    @Override
    public Card updateCardMain(Long card_id, boolean cardMain) {
        existsCardByCardId(card_id);
        Card card = cardRepository.findById(card_id).get();
        updateMain(card.getUser().getId());
        card.setCardMain(cardMain);
        return cardRepository.save(card);
    }

    @Override
    public List<Card> getCardByUserId(Long user_id) {
        existsUserByUserId(user_id);
        return cardRepository.findByUserId(user_id);
    }

    private void updateMain(Long userId) {
        List<Card> cards = cardRepository.findByUserId(userId);
        for (Card card :
                cards) {
            if (card.isCardMain()) {
                card.setCardMain(false);
                cardRepository.save(card);
            }
        }
    }

    private void existsCardByCardId(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new ResourceNotFoundException("Card with id " + cardId + " not found");
        }
    }

    private void existsUserByUserId(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                () -> new ResourceNotFoundException("User not found with id " + userId));
    }

    private void existsCardByCardNumber(Card card) {
        if (cardRepository.existsByCardNumber(card.getCardNumber())) {
            throw new ValidationException("Card number already exists");
        }
    }

    private boolean firstCard(Long userId){
        return cardRepository.findByUserId(userId).isEmpty();
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
//        if (!(card.getCardType().equals("visa")) && !(card.getCardType().equals("mastercard"))){
//            throw new ValidationException("Card can only be visa or mastercard");
//        }
    }
}
