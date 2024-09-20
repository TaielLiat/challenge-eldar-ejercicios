package com.eldar.challenge.ejercicio2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Column(nullable = false)
    private String name;

    @Email(message = "El email debe ser válido.")
    @NotBlank(message = "El email no puede estar vacío.")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener entre 7 y 8 dígitos.")
    @Column(nullable = false, unique = true)
    private String dni;

    @NotNull(message = "La fecha de nacimiento no puede estar vacía.")
    @Past(message = "La fecha de nacimiento debe ser anterior a la actual.")
    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Card> cards;
}