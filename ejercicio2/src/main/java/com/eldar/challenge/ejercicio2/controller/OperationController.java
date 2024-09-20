package com.eldar.challenge.ejercicio2.controller;

import com.eldar.challenge.ejercicio2.entity.Card;
import com.eldar.challenge.ejercicio2.entity.Operation;
import com.eldar.challenge.ejercicio2.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operations")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @GetMapping("/rate")
    public ResponseEntity<String> getRate(@RequestParam String brand, @RequestParam Double amount) {
        try {
            Card tempCard = new Card();
            tempCard.setBrand(brand);

            double rate = tempCard.calculateRate();
            double additionalCost = amount * rate / 100;

            String message = String.format(
                    "Marca: %s%n" +
                            "Tasa: %.2f%%%n" +
                            "Coste adicional de la operación: $%.2f",
                    brand, rate, additionalCost
            );
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Marca de tarjeta inválida.");
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> processPurchase(@RequestBody Operation operation, @RequestParam String cvv) {
        try {
            Operation processedOperation = operationService.createOperation(operation, cvv);
            return ResponseEntity.ok("Compra procesada con éxito. Se enviará una notificación por correo con los detalles.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
