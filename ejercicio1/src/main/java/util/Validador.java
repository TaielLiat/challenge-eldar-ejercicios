package util;

import excepciones.ValidadorExcepcion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.regex.Pattern;


public class Validador {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void validarCliente(String nombre, String apellido, String dni, String fechaNacimiento, String email) throws ValidadorExcepcion {
        validarNombre(nombre);
        validarApellido(apellido);
        validarDni(dni);
        validarFechaNacimiento(fechaNacimiento);
        validarEmail(email);
    }

    public static void validarTarjeta(String marca, String numero, String fechaVencimiento, Set<String> marcasValidas) throws ValidadorExcepcion {
        if (!marcasValidas.contains(marca.toUpperCase())) {
            throw new ValidadorExcepcion("Marca inválida: " + marca);
        }
        if (numero == null || numero.length() < 13) {
            throw new ValidadorExcepcion("Número de tarjeta inválido.");
        }
        validarFechaVencimiento(fechaVencimiento);
    }

    public static void validarNombre(String nombre) throws ValidadorExcepcion {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidadorExcepcion("El nombre no puede estar vacío.");
        }
        if (!NAME_PATTERN.matcher(nombre).matches()) {
            throw new ValidadorExcepcion("El nombre no puede contener caracteres especiales o números.");
        }
    }

    public static void validarApellido(String apellido) throws ValidadorExcepcion {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ValidadorExcepcion("El apellido no puede estar vacío.");
        }
        if (!NAME_PATTERN.matcher(apellido).matches()) {
            throw new ValidadorExcepcion("El apellido no puede contener caracteres especiales o números.");
        }
    }

    public static void validarDni(String dni) throws ValidadorExcepcion {
        if (dni == null || dni.trim().isEmpty()) {
            throw new ValidadorExcepcion("El DNI no puede estar vacío.");
        }
        int longitud = dni.length();
        if (!dni.matches("\\d+") || longitud < 6 || longitud > 8) {
            throw new ValidadorExcepcion("DNI inválido. Debe contener entre 6 y 8 dígitos numéricos.");
        }
    }

    public static void validarFecha(String fechaStr) throws ValidadorExcepcion {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate.parse(fechaStr, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ValidadorExcepcion("La fecha es inválida: " + fechaStr);
        }
    }

    public static void validarFechaVencimiento(String fechaStr) throws ValidadorExcepcion {
        validarFecha(fechaStr);

        LocalDate fecha = LocalDate.parse(fechaStr, FORMATTER);
        if (fecha.isBefore(LocalDate.now())) {
            throw new ValidadorExcepcion("La fecha de vencimiento no puede ser anterior a la fecha actual.");
        }
    }

    public static void validarFechaNacimiento(String fechaStr) throws ValidadorExcepcion {
        validarFecha(fechaStr);

        LocalDate fecha = LocalDate.parse(fechaStr, FORMATTER);
        if (fecha.isAfter(LocalDate.now())) {
            throw new ValidadorExcepcion("La fecha de nacimiento no puede ser posterior a la fecha actual.");
        }
    }
    public static void validarEmail(String email) throws ValidadorExcepcion {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidadorExcepcion("El email no puede estar vacío.");
        }
        if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$")) {
            throw new ValidadorExcepcion("El formato del email es inválido.");
        }
    }
}
