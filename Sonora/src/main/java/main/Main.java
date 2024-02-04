package main;

import java.util.List;

import dao.DiscoDAO;
import io.IO;
import modelo.Disco;

public class Main {
    public static void main(String[] args) {
        DiscoDAO discoDAO = new DiscoDAO();


        while (true) {
            IO.println("\nMenú Principal:");
            IO.println("1. Insertar nuevo disco");
            IO.println("2. Mostrar todos los discos");
            IO.println("3. Salir");
            IO.print("Seleccione una opción: ");

            int opcion = IO.readInt();

            switch (opcion) {
                case 1:
                    insertarDisco(discoDAO);
                    break;
                case 2:
                    mostrarTodosLosDiscos(discoDAO);
                    break;
                case 3:
                    IO.println("Saliendo...");
                    return;
                default:
                    IO.println("Opción no válida, intente de nuevo.");
                    break;
            }
        }
    }
    private static void mostrarTodosLosDiscos(DiscoDAO discoDAO) {
        IO.println("\nLista de todos los discos:");
        List<Disco> discos = discoDAO.obtenerTodosLosDiscos();
        if (discos.isEmpty()) {
            IO.println("No hay discos registrados en la base de datos.");
        } else {
            for (Disco disco : discos) {
                IO.println(disco.toString()); // Asegúrate de que el método toString de Disco esté implementado adecuadamente
            }
        }
    }

    private static void insertarDisco(DiscoDAO discoDAO) {
        IO.println("Insertar nuevo Disco:");

        IO.print("Nombre del Disco: ");
        String nombreDisco = IO.readString();

        IO.print("Artista: ");
        String artista = IO.readString();

        IO.print("Precio: ");
        double precio = IO.readDouble();

        IO.print("Ingresado/Retirado por: ");
        String ingresadoRetiradoPor = IO.readString();

        // Así sucesivamente para el resto de campos, utilizando IO.read...() según corresponda
        Disco disco = Disco.builder()
                .nombreDisco(nombreDisco)
                .artista(artista)
                .precio(precio)
                .ingresadoRetiradoPor(ingresadoRetiradoPor)
                // Campos opcionales aquí, condicionales según sea necesario
                .build();

        discoDAO.insertarDisco(disco);
        IO.println("Disco insertado con éxito.");
    }
}
