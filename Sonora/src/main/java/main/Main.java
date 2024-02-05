package main;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import dao.DiscoDAO;
import io.IO;
import modelo.Disco;
import util.MongoDBUtil;

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
                    seleccionarYMostrarMenuDisco(discoDAO);
                    break;
                case 3: // Asumiendo que 3 es la opción para salir
                    IO.println("Programa finalizado con éxito, conexión a MongoDB cerrada.");
                    MongoDBUtil.close(); // Cierra la conexión a la base de datos antes de salir
                    return;
                default:
                    IO.println("Opción no válida, intente de nuevo.");
                    break;
            }
        }
    }
    private static void mostrarTodosLosDiscos(DiscoDAO discoDAO) {
        IO.println("\nEstos son los discos encontrados:");
        List<Disco> discos = discoDAO.obtenerTodosLosDiscos();
        if (discos.isEmpty()) {
            IO.println("No hay discos registrados en la base de datos.");
        } else {
            int index = 1;
            for (Disco disco : discos) {
                IO.println(index + ". " + disco.getNombreDisco() + ", " + disco.getArtista() + ", Retirado/Ingresado por: " + disco.getIngresadoRetiradoPor() + ", " + obtenerCamposOpcionales(disco));
                index++;
            }
        }
    }
    
    private static String obtenerCamposOpcionales(Disco disco) {
        // Este método construye una cadena con los campos opcionales no nulos
        String campos = "";
        if (disco.getGenero() != null) campos += "Género: " + disco.getGenero() + ", ";
        // Repite para otros campos opcionales
        // Elimina la última coma y espacio si hay campos
        if (!campos.isEmpty()) campos = campos.substring(0, campos.length() - 2);
        return campos;
    }
    
    private static void seleccionarYMostrarMenuDisco(DiscoDAO discoDAO) {
        IO.println("Seleccione el número del disco para más opciones o 0 para volver:");
        int seleccion = IO.readInt();
        
        if (seleccion == 0) return; // Volver al menú principal si el usuario ingresa 0
        
        List<Disco> discos = discoDAO.obtenerTodosLosDiscos();
        if (seleccion > 0 && seleccion <= discos.size()) {
            Disco discoSeleccionado = discos.get(seleccion - 1);
            mostrarMenuDisco(discoSeleccionado, discoDAO); // Ajustado para pasar discoDAO también
        } else {
            IO.println("Selección inválida.");
        }
    }


    private static void mostrarMenuDisco(Disco disco, DiscoDAO discoDAO) {
        IO.println("Has seleccionado: " + disco.getNombreDisco());
        IO.println("1. Editar disco");
        IO.println("2. Retirar disco");
        IO.println("3. Volver al menú principal");

        IO.print("Seleccione una opción: ");
        int opcion = IO.readInt();

        switch (opcion) {
            case 1:
            	editarDisco(discoDAO, disco);
                break;
            case 2:
                IO.print("Ingrese su nombre de usuario para confirmar el retiro: ");
                String nombreUsuario = IO.readString();
                discoDAO.retirarDisco(disco.getNombreDisco(), nombreUsuario);
                break;
            case 3:
                // Simplemente sale del método
                break;
            default:
                IO.println("Opción no válida, intente de nuevo.");
                break;
        }
    }




    private static void insertarDisco(DiscoDAO discoDAO) {
        IO.println("Ingrese el nombre del disco:");
        String nombreDisco = IO.readString();
        IO.println("Ingrese el artista:");
        String artista = IO.readString();
        IO.println("Ingrese el precio:");
        double precio = IO.readDouble(); // Asegúrate de manejar correctamente la entrada de números.
        IO.println("Ingresado/Retirado por:");
        String ingresadoRetiradoPor = IO.readString();

        Disco.DiscoBuilder builder = Disco.builder()
                .nombreDisco(nombreDisco)
                .artista(artista)
                .precio(precio)
                .ingresadoRetiradoPor(ingresadoRetiradoPor);

        IO.println("¿Desea ingresar más información sobre el disco? (s/n):");
        String respuesta = IO.readString().trim().toLowerCase();
        
        while ("s".equals(respuesta)) {
            mostrarMenuAtributosOpcionales();
            int opcion = IO.readInt();
            switch (opcion) {
                case 1:
                    IO.println("Ingrese el género:");
                    builder.genero(IO.readString());
                    break;
                case 2:
                    IO.println("Ingrese la fecha de lanzamiento (formato YYYY-MM-DD):");
                    try {
                        builder.fechaLanzamiento(LocalDate.parse(IO.readString()));
                    } catch (DateTimeParseException e) {
                        IO.println("Formato de fecha no válido. Intenta nuevamente.");
                    }
                    break;
                case 9:
                    respuesta = "n"; // Terminar de añadir campos opcionales.
                    break;
                default:
                    IO.println("Opción no válida, intente de nuevo.");
                    break;
            }
            if (opcion != 9) { // Solo preguntar si no se ha decidido terminar.
                IO.println("¿Desea añadir más información? (s/n):");
                respuesta = IO.readString().trim().toLowerCase();
            }
        }

        Disco disco = builder.build();
        discoDAO.insertarDisco(disco);
        IO.println("Disco insertado con éxito.");
    }
    
    private static void editarDisco(DiscoDAO discoDAO, Disco disco) {
        boolean seguirEditando = true;
        while (seguirEditando) {
            IO.println("¿Qué campo le gustaría editar?");
            IO.println("1. Nombre del Disco");
            IO.println("2. Artista");
            IO.println("3. Precio");
            IO.println("4. Ingresado/Retirado por");
            IO.println("5. Género");
            IO.println("6. Fecha de Lanzamiento");
            // Agrega más campos opcionales según tu modelo Disco
            IO.println("7. Finalizar Edición");
            
            int opcion = IO.readInt();
            switch (opcion) {
                case 1:
                    IO.println("Ingrese el nuevo nombre del disco:");
                    disco.setNombreDisco(IO.readString());
                    break;
                case 2:
                    IO.println("Ingrese el nuevo artista:");
                    disco.setArtista(IO.readString());
                    break;
                case 3:
                    IO.println("Ingrese el nuevo precio:");
                    disco.setPrecio(IO.readDouble());
                    break;
                case 4:
                    IO.println("Ingresado/Retirado por:");
                    disco.setIngresadoRetiradoPor(IO.readString());
                    break;
                case 5:
                    IO.println("Ingrese el nuevo género:");
                    disco.setGenero(IO.readString());
                    break;
                case 6:
                    IO.println("Ingrese la nueva fecha de lanzamiento (formato YYYY-MM-DD):");
                    disco.setFechaLanzamiento(LocalDate.parse(IO.readString()));
                    break;
                case 7:
                    seguirEditando = false;
                    break;
                default:
                    IO.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
        discoDAO.actualizarDisco(disco);
        IO.println("Disco actualizado con éxito.");
    }



    
    private static void mostrarMenuAtributosOpcionales() {
        IO.println("Seleccione el atributo que desea añadir:");
        IO.println("1. Género");
        IO.println("2. Fecha de Lanzamiento");
        // Lista otros atributos opcionales aquí
        IO.println("9. Finalizar y guardar");
    }

}
