package com.eldar.challenge.ejercicio2.repository;

import com.eldar.challenge.ejercicio2.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    Optional<Card> findByUserId(UUID id);

    Optional<Card> findByCardNumber(String cardNumber);
}