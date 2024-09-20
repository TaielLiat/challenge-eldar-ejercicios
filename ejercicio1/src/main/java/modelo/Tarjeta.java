package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarjeta {
    private String marca;
    private String numero;
    private LocalDate fechaVencimiento;
    private Cliente titular;
    private String cvv;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Tarjeta(String marca, String numero, String fechaVencimiento, Cliente titular) {
        this.marca= marca;
        this.numero = numero;
        this.fechaVencimiento = parseFechaVencimiento(fechaVencimiento);
        this.titular = titular;
        this.cvv = generarCvv(numero);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía.");
        }
        this.marca = marca;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = parseFechaVencimiento(fechaVencimiento);
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    private LocalDate parseFechaVencimiento(String fechaVencimiento) {
        return LocalDate.parse(fechaVencimiento, FORMATTER);
    }

    private String generarCvv(String numero) {
        try {
            int num1 = Character.getNumericValue(numero.charAt(7));
            int num2 = Character.getNumericValue(numero.charAt(11));
            int num3 = Character.getNumericValue(numero.charAt(12));
            return String.valueOf(num1 + num2 + num3 + 1);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número de tarjeta contiene caracteres no numéricos.");
        }
    }

    public boolean esValida() {
        return fechaVencimiento.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Tarjeta " + marca + " N° " + numero + " - Vence el " + fechaVencimiento.format(FORMATTER) + " - CVV: " + cvv;
    }
}
