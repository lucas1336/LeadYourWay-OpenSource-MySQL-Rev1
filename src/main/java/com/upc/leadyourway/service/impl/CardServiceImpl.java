package com.upc.leadyourway.service.impl;

import com.upc.leadyourway.model.Card;
import com.upc.leadyourway.repository.CardRepository;
import com.upc.leadyourway.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card getCardById(Long card_id) {
        return cardRepository.findById(card_id).orElse(null);
    }

    @Override
    public Card updateCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long card_id) {
        cardRepository.deleteById(card_id);
    }
}
