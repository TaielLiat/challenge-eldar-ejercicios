package com.eldar.challenge.ejercicio2.service;

import com.eldar.challenge.ejercicio2.entity.User;
import com.eldar.challenge.ejercicio2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws Exception{
        Optional<User> existingUser = userRepository.findByDni(user.getDni());
        if (existingUser.isPresent()) {
            throw new Exception("El DNI ya está registrado para otro usuario.");
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) throws Exception {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }
        return userRepository.findById(id);
    }

    public Optional<User> getUserByDni(String dni) {
        return userRepository.findByDni(dni);
    }

    public User updateUser(UUID id, User updatedUser) throws Exception {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }

        Optional<User> owner = userRepository.findByDni(updatedUser.getDni());
        if (owner.isPresent() && !owner.get().getId().equals(id)) {
            throw new Exception("El DNI ingresado corresponde a otro usuario.");
        }

        User user = existingUser.get();
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setDni(updatedUser.getDni());
        user.setBirthDate(updatedUser.getBirthDate());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) throws Exception {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }
        userRepository.deleteById(id);
    }
}