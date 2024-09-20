package com.eldar.challenge.ejercicio2;

import com.eldar.challenge.ejercicio2.entity.Card;
import com.eldar.challenge.ejercicio2.entity.Operation;
import com.eldar.challenge.ejercicio2.entity.User;
import com.eldar.challenge.ejercicio2.repository.OperationRepository;
import com.eldar.challenge.ejercicio2.service.CardService;
import com.eldar.challenge.ejercicio2.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private CardService cardService;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private OperationService operationService;

    private Card card;
    private Operation operation;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
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
        card.setCvv("123");
        card.setExpiryDate(LocalDate.now().plusYears(1));
        card.setBrand("VISA");
        card.setUser(user);

        operation = new Operation();
        operation.setId(UUID.randomUUID());
        operation.setCard(card);
        operation.setUser(user);
        operation.setAmount(5000.0);
        operation.setDate(LocalDate.now());

        when(cardService.getCardById(card.getId())).thenReturn(Optional.of(card));
    }

    @Test
    void testCreateOperation() throws Exception {
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);

        Operation createdOperation = operationService.createOperation(operation, "123");

        assertNotNull(createdOperation);
        assertEquals(5000.0, createdOperation.getAmount());
        verify(operationRepository, times(1)).save(operation);
    }

    @Test
    void testInvalidCVV() { // CVV incorrecto
        Exception exception = assertThrows(Exception.class, () -> {
            operationService.createOperation(operation, "999");
        });

        assertEquals("El CVV ingresado es incorrecto.", exception.getMessage());
    }

    @Test
    void testExpiredCard() {
        card.setExpiryDate(LocalDate.now().minusDays(1));

        Exception exception = assertThrows(Exception.class, () -> {
            operationService.createOperation(operation, "123");
        });

        assertEquals("La tarjeta asociada está vencida.", exception.getMessage());
    }

    @Test
    void testExcessiveAmount() {
        operation.setAmount(15000.0); // limite actual 10,000

        Exception exception = assertThrows(Exception.class, () -> {
            operationService.createOperation(operation, "123");
        });

        assertEquals("El monto de la operación excede los $10.000 pesos.", exception.getMessage());
    }



}