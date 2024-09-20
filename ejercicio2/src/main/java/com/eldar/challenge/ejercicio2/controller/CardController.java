package com.eldar.challenge.ejercicio2.controller;

import com.eldar.challenge.ejercicio2.entity.Card;
import com.eldar.challenge.ejercicio2.service.CardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/{userId}")
    public ResponseEntity<Card> createCard(@PathVariable UUID userId, @Valid @RequestBody Card card) throws Exception {
        Card newCard = cardService.createCard(card, userId);
        return ResponseEntity.status(201).body(newCard);
    }

    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable UUID id) throws Exception {
        Optional<Card> card = cardService.getCardById(id);
        return ResponseEntity.ok(card.get());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Card> getCardByUserId(@PathVariable UUID id) throws Exception {
        Optional<Card> card = cardService.getCardByUserId(id);
        return ResponseEntity.ok(card.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable UUID id, @Valid @RequestBody Card card) throws Exception {
        Card updatedCard = cardService.updateCard(id, card);
        return ResponseEntity.ok(updatedCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) throws Exception {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}