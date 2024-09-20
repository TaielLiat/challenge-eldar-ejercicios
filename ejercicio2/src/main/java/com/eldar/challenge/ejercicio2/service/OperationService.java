package com.eldar.challenge.ejercicio2.service;

import com.eldar.challenge.ejercicio2.entity.Card;
import com.eldar.challenge.ejercicio2.entity.Operation;
import com.eldar.challenge.ejercicio2.entity.User;
import com.eldar.challenge.ejercicio2.repository.OperationRepository;
import com.eldar.challenge.ejercicio2.utils.EmailSender;
import com.eldar.challenge.ejercicio2.utils.Validations;
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
    private Validations validations;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private CardService cardService;

    @Autowired
    private JavaMailSender mailSender;


    public Operation createOperation(Operation operation, String cvv) throws Exception {
        Card card = operation.getCard();
        validations.validateCard(card, cvv);
        validations.validateOperationAmount(operation.getAmount());

        double rate = card.calculateRate();
        double fee = operation.getAmount() * rate / 100;// Calcular la comision
        operation.setFee(fee);

        Operation savedOperation = operationRepository.save(operation);

        emailSender.sendPurchaseNotification(savedOperation);

        return savedOperation;
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    public Optional<Operation> getOperationById(UUID id) {
        return operationRepository.findById(id);
    }
}