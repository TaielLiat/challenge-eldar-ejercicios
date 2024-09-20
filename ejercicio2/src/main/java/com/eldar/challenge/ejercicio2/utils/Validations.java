package com.eldar.challenge.ejercicio2.utils;

import com.eldar.challenge.ejercicio2.entity.Card;
import com.eldar.challenge.ejercicio2.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Validations {

    @Autowired
    private CardService cardService;

    String SECRET_KEY = System.getenv("SECRET_KEY");

    public void validateCard(Card card, String cvv) throws Exception {
        String decryptedCVV = Encryption.decryptCVV(card.getCvv(), SECRET_KEY);

        if (card.getId() == null || !cardService.getCardById(card.getId()).isPresent()) {
            throw new Exception("La tarjeta asociada no existe en el sistema.");
        }
        if (!decryptedCVV.equals(cvv)) {
            throw new Exception("El CVV ingresado es incorrecto.");
        }
        if (card.isExpired()) {
            throw new Exception("La tarjeta está vencida.");
        }
    }

    public void validateOperationAmount(double amount) throws Exception {
        if (amount > 10000) {
            throw new Exception("El monto de la operación excede los $10.000 pesos.");
        }
    }
}
