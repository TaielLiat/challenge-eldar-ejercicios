import excepciones.ValidadorExcepcion;
import modelo.Tarjeta;
import servicio.TarjetaServicio;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final TarjetaServicio ts = new TarjetaServicio();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n##### ELDAR OPERACIONES #####");

        while (true) {
            Menu();
            String opcion = scanner.nextLine();

            try {
                procesarOpcion(opcion);
            } catch (ValidadorExcepcion e) {
                System.out.println("Error de validación: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error de argumento: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            }
        }
    }

    private static void registrarCliente() {
        String nombre = Entrada("Ingrese el nombre: ");
        String apellido = Entrada("Ingrese el apellido: ");
        String dni = Entrada("Ingrese el DNI: ");
        String fechaNacimiento = Entrada("Ingrese la fecha de nacimiento (dd-MM-yyyy): ");
        String email = Entrada("Ingrese el email: ");

        ts.registrarCliente(nombre, apellido, dni, fechaNacimiento, email);
        System.out.println("Cliente registrado con éxito.");

    }

    private static void registrarTarjeta()  {
        String dni = Entrada("Ingrese el DNI del titular: ");
        String marca = Entrada("Ingrese la marca (VISA, NARA, AMEX): ").toUpperCase();
        String numero = Entrada("Ingrese el número de la tarjeta: ");
        String fechaVencimiento = Entrada("Ingrese la fecha de vencimiento (dd-MM-yyyy): ");

        ts.registrarTarjeta(dni, marca, numero, fechaVencimiento);
        System.out.println("Tarjeta registrada con éxito.");
    }

    private static void consultarTarjetas()  {
        String dni = Entrada("Ingrese el DNI de la persona: ");
        List<Tarjeta> tarjetas = ts.obtenerTarjetas(dni);

        if (tarjetas.isEmpty()) {
            System.out.println("No hay tarjetas asociadas.");
        } else {
            System.out.println("Tarjetas asociadas:");
            tarjetas.forEach(System.out::println);
        }
    }

    private static void consultarTasas() {
        String fechaStr = Entrada("Ingrese la fecha (dd-MM-yyyy):");
        ts.consultarTasas(fechaStr);
    }

    private static void procesarOpcion(String opcion) throws ValidadorExcepcion {
        switch (opcion) {
            case "1":
                registrarCliente();
                break;
            case "2":
                registrarTarjeta();
                break;
            case "3":
                consultarTarjetas();
                break;
            case "4":
                consultarTasas();
                break;
            case "5":
                System.out.println("Gracias por utilizar ELDAR OPERACIONES, hasta la próxima.");
                System.exit(0);
            default:
                System.out.println("Opción inválida. Intentelo nuevamente.");
        }
    }

    private static void Menu() {
        System.out.println("\n--- Menú ---");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Registrar tarjeta");
        System.out.println("3. Consultar tarjetas por DNI");
        System.out.println("4. Consultar tasas");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static String Entrada(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}