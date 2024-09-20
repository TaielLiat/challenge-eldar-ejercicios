package com.eldar.challenge.ejercicio2;

import com.eldar.challenge.ejercicio2.entity.Card;
import com.eldar.challenge.ejercicio2.entity.User;
import com.eldar.challenge.ejercicio2.repository.CardRepository;
import com.eldar.challenge.ejercicio2.repository.UserRepository;
import com.eldar.challenge.ejercicio2.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Martin Colapinto");
        user.setEmail("martincolapinto@gmail.com");
        user.setDni("12345678");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        card = new Card();
        card.setId(UUID.randomUUID());
        card.setCardNumber("1234567812345678");
        card.setBrand("VISA");
        card.setCvv("123");
        card.setExpiryDate(LocalDate.now().plusYears(1)); // Fecha de vencimiento válida
        card.setUser(user);
    }

    @Test
    void testCreateCardForNonExistentUser() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            cardService.createCard(card, UUID.randomUUID());
        });

        assertEquals("Usuario no encontrado.", exception.getMessage());
    }
}