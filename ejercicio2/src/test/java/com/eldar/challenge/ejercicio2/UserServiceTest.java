package com.eldar.challenge.ejercicio2;

import com.eldar.challenge.ejercicio2.entity.User;
import com.eldar.challenge.ejercicio2.repository.UserRepository;
import com.eldar.challenge.ejercicio2.service.UserService;
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
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import jakarta.validation.ConstraintViolation;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Martin Colapinto");
        user.setEmail("martincolapinto@gmail.com");
        user.setDni("12345678");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        // Crear el validador
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateUser() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Martin Colapinto", createdUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUserWithEmptyName() {
        user.setName(""); // Nombre vacío

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("El nombre no puede estar vacío.", violation.getMessage());
    }

    @Test
    void testCreateUserWithInvalidEmail() {
        user.setEmail("invalid-email"); // Email inválido

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("El email debe ser válido.", violation.getMessage());
    }

    @Test
    void testCreateUserWithEmptyDni() {
        user.setDni(""); // DNI vacío

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("El DNI debe tener entre 7 y 8 dígitos.", violation.getMessage());
    }

    @Test
    void testCreateUserWithInvalidDni() {
        user.setDni("123"); // DNI inválido

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("El DNI debe tener entre 7 y 8 dígitos.", violation.getMessage());
    }

    @Test
    void testCreateUserWithFutureBirthDate() {
        user.setBirthDate(LocalDate.now().plusDays(1)); // Fecha de nacimiento futura

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("La fecha de nacimiento debe ser anterior a la actual.", violation.getMessage());
    }
}
