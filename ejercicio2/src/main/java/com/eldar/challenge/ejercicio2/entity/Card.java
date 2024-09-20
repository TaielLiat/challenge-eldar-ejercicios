package com.eldar.challenge.ejercicio2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Card {

    private static final double MIN_TASA = 0.3;
    private static final double MAX_TASA = 5.0;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "El número de tarjeta no puede estar vacío.")
    @Pattern(regexp = "\\d{16}", message = "El número de tarjeta debe tener 16 dígitos numéricos.")
    @Column(nullable = false, unique = true)
    private String cardNumber;

    @NotBlank(message = "La marca de la tarjeta no puede estar vacía.")
    @Pattern(regexp = "VISA|NARA|AMEX", message = "La marca debe ser VISA, NARA o AMEX.")
    @Column(nullable = false)
    private String brand;

    @NotNull(message = "La fecha de vencimiento no puede estar vacía.")
    @Future(message = "La fecha de vencimiento debe ser una posterior a la actual.")
    @Column(nullable = false)
    private LocalDate expiryDate;

    @NotBlank(message = "El CVV no puede estar vacío.")
    @Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos.")
    @Column(nullable = false)
    private String cvv;  // CVV cifrado

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public double calculateRate() {
        LocalDate today = LocalDate.now();

        double rate = switch (brand.toUpperCase()) {
            case "VISA" -> (double) today.getYear() / today.getMonthValue();
            case "NARA" -> today.getDayOfMonth() * 0.5;
            case "AMEX" -> today.getMonthValue() * 0.1;
            default -> throw new IllegalArgumentException("Marca de tarjeta desconocida: " + brand);
        };

        return Math.max(MIN_TASA, Math.min(rate, MAX_TASA));
    }
}