package com.upc.leadyourway.service;

import com.upc.leadyourway.model.Card;

public interface CardService {
    public abstract Card createCard(Card card);
    public abstract Card getCardById(Long card_id);
    public abstract Card updateCard(Card card);
    public abstract void deleteCard(Long card_id);
}
