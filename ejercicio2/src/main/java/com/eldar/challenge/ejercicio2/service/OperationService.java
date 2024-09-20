package com.eldar.challenge.ejercicio2.service;

import com.eldar.challenge.ejercicio2.entity.Card;
import com.eldar.challenge.ejercicio2.entity.Operation;
import com.eldar.challenge.ejercicio2.entity.User;
import com.eldar.challenge.ejercicio2.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private JavaMailSender mailSender;

    public Operation createOperation(Operation operation, String cvv) throws Exception {
        Card card = operation.getCard();

        validateOperationData(operation, cvv);

        double rate = card.calculateRate();
        double fee = operation.getAmount() * rate / 100;// Calcular la comision
        operation.setFee(fee);

        Operation savedOperation = operationRepository.save(operation);

        //sendPurchaseNotification(savedOperation);

        return savedOperation;
    }

    private void validateOperationData(Operation operation, String cvv) throws Exception {
        Card card = operation.getCard();

        if (!card.getCvv().equals(cvv)) {
            throw new Exception("El CVV ingresado es incorrecto.");
        }
        if (card.isExpired()) {
            throw new Exception("La tarjeta asociada está vencida.");
        }
        if (!operation.isAmountValid()) {
            throw new Exception("El monto de la operación excede los $10.000 pesos.");
        }
        if (card.getId() == null || !cardService.getCardById(card.getId()).isPresent()) {
            throw new Exception("La tarjeta asociada no existe en el sistema. Por favor ingrese la tarjeta al sistema e inténtelo nuevamente.");
        }
    }

    private void sendPurchaseNotification(Operation operation) {
        User user = operation.getUser();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirmación de compra");
        message.setText("Hola " + user.getName() + ",\n\n¡Tu pago se procesó exitosamente!. \n\nDetalles de la operación " +
                "\n\n Costo de productos:"+ operation.getAmount() +
                "\n\n Comisión:"+ operation.getFee() +
                ".\n\nGracias por usar nuestros servicios.");

        mailSender.send(message);
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    public Optional<Operation> getOperationById(UUID id) {
        return operationRepository.findById(id);
    }
}