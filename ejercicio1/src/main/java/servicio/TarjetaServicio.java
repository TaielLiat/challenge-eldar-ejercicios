package servicio;

import excepciones.ValidadorExcepcion;
import modelo.Cliente;
import modelo.Tarjeta;
import util.Validador;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TarjetaServicio {
    private final Map<String, Cliente> clientes = new HashMap<>();
    private final Map<String, List<Tarjeta>> tarjetas = new HashMap<>();
    private final Set<String> marcasValidas = new HashSet<>();
    private Properties tasas;
    private static final double MIN_TASA = 0.3;
    private static final double MAX_TASA = 5.0;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TarjetaServicio() {
        tasas = new Properties();
        try {
            cargarTasas();
        } catch (IOException e) {
            System.err.println("Error crítico al cargar tasas: " + e.getMessage());
        }
    }

    private void cargarTasas() throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("tasas.properties")) {
            if (input == null) {
                throw new IOException("No se encontró el archivo tasas.properties en el classpath.");
            }
            tasas.load(input);
            tasas.stringPropertyNames().forEach(key -> marcasValidas.add(key.toUpperCase()));
        }
    }

    public void registrarCliente(String nombre, String apellido, String dni, String fechaNacimiento, String email) {
        try {
            Validador.validarCliente(nombre, apellido, dni, fechaNacimiento, email);
            if (clientes.containsKey(dni)) {
                throw new ValidadorExcepcion("El cliente con DNI " + dni + " ya está registrado.");
            }
            clientes.put(dni, new Cliente(nombre, apellido, dni, fechaNacimiento, email));
        } catch (ValidadorExcepcion e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
        }
    }

    public void registrarTarjeta(String dni, String marca, String numero, String fechaVencimiento)  {
        try {
            Cliente titular = Optional.ofNullable(clientes.get(dni))
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el cliente con DNI: " + dni));

            Validador.validarTarjeta(marca, numero, fechaVencimiento, marcasValidas);
            Tarjeta tarjeta = new Tarjeta(marca, numero, fechaVencimiento, titular);
            tarjetas.computeIfAbsent(dni, k -> new ArrayList<>()).add(tarjeta);
        } catch (IllegalArgumentException | ValidadorExcepcion e) {
            System.err.println("Error al registrar tarjeta: " + e.getMessage());
        }
    }

    public List<Tarjeta> obtenerTarjetas(String dni) {
        try {
            Validador.validarDni(dni);
            return Optional.ofNullable(tarjetas.get(dni))
                    .orElseThrow(() -> new IllegalArgumentException("No hay tarjetas asociadas al DNI: " + dni));
        } catch (IllegalArgumentException | ValidadorExcepcion e) {
            System.err.println("Error al obtener tarjetas: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void consultarTasas(String fechaStr) {
        LocalDate fecha;
        try {
            if (fechaStr == null || fechaStr.isEmpty()) {
                fecha = LocalDate.now();
            } else {
                Validador.validarFecha(fechaStr);
                fecha = LocalDate.parse(fechaStr, FORMATTER);
            }

            System.out.println("Fecha: " + fecha.format(FORMATTER) + ")");
            for (String marca : marcasValidas) {
                double tasa = calcularTasa(fecha, marca);
                tasa = Math.max(MIN_TASA, Math.min(tasa, MAX_TASA)); // Formatea min y max tasa a los limites
                System.out.println("Marca: " + marca + " - Tasa: " + String.format("%.2f", tasa) + "%");
            }
        } catch (ValidadorExcepcion e) {
            System.err.println("Error al consultar tasas: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la fecha: " + e.getMessage());
        }
    }

    public double calcularTasa(LocalDate fecha, String marca) {
        try {
            int dia = fecha.getDayOfMonth();
            int mes = fecha.getMonthValue();
            int ano = fecha.getYear() % 100;

            return switch (marca.toUpperCase()) {
                case "VISA" -> (ano / (double) mes);
                case "NARA" -> dia * 0.5;
                // El valor parece ser mayor al indicado en el Challenge, ¿Debería limitarlo?
                // Tasa NARA = dia del mes *0.5 (Ejemplo: 27 * 0.5) > 13,5%
                case "AMEX" -> mes * 0.1;
                default -> throw new IllegalArgumentException("Marca desconocida: " + marca);
            };
        } catch (IllegalArgumentException e) {
            System.err.println("Error al calcular tasa: " + e.getMessage());
            return MIN_TASA;
        }
    }
}

