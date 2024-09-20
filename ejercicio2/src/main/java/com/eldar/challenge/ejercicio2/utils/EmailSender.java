package com.eldar.challenge.ejercicio2.utils;

import com.eldar.challenge.ejercicio2.entity.Operation;
import com.eldar.challenge.ejercicio2.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPurchaseNotification(Operation operation) {
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
}
