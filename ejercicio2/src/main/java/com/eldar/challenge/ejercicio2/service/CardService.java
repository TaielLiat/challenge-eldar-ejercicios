package com.eldar.challenge.ejercicio2.service;

import com.eldar.challenge.ejercicio2.entity.Card;
import com.eldar.challenge.ejercicio2.entity.User;
import com.eldar.challenge.ejercicio2.repository.CardRepository;
import com.eldar.challenge.ejercicio2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    public Card createCard(Card card, UUID userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }
        Optional<Card> owner = cardRepository.findByCardNumber(card.getCardNumber());
        if (owner.isPresent() && !owner.get().getId().equals(card.getId())) {
            throw new Exception("La tarjeta ingresada ya existe en el sistema.");
        }

        card.setUser(user.get());
        String encryptedCVV = encryptCVV(card.getCvv());
        card.setCvv(encryptedCVV);

        return cardRepository.save(card);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> getCardById(UUID id) throws Exception {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isEmpty()) {
            throw new Exception("Tarjeta no encontrada.");
        }
        return card;
    }

    public Optional<Card> getCardByUserId(UUID id) throws Exception {
        Optional<Card> card = cardRepository.findByUserId(id);
        if (card.isEmpty()) {
            throw new Exception("No se encontraron tarjetas a nombre del usuario.");
        }
        return card;
    }

    public Card updateCard(UUID id, Card updatedCard) throws Exception {
        Optional<Card> existingCard = cardRepository.findById(id);
        if (existingCard.isEmpty()) {
            throw new Exception("Tarjeta no encontrada.");
        }

        Card card = existingCard.get();
        validateCardData(updatedCard);
        card.setCardNumber(updatedCard.getCardNumber());
        card.setBrand(updatedCard.getBrand());
        card.setExpiryDate(updatedCard.getExpiryDate());
        card.setCvv(encryptCVV(updatedCard.getCvv()));

        return cardRepository.save(card);
    }

    public void deleteCard(UUID id) throws Exception {
        Optional<Card> existingCard = cardRepository.findById(id);
        if (existingCard.isEmpty()) {
            throw new Exception("Tarjeta no encontrada.");
        }
        cardRepository.deleteById(id);
    }

    private void validateCardData(Card card) throws Exception {
        if (card.isExpired()) {
            throw new Exception("La tarjeta está vencida.");
        }
    }

    private String encryptCVV(String cvv) {
        return cvv;
    }


}