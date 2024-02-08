package main;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            IO.println("3. Buscar discos");
            IO.println("4. Salir");
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
                case 3:
                    // Lógica para buscar discos con filtros
                    mostrarMenuBusqueda(discoDAO);
                    break;
                case 4: // Asumiendo que 3 es la opción para salir
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
            for (int index = 0; index < discos.size(); index++) {
                Disco disco = discos.get(index);
                String camposOpcionales = obtenerCamposOpcionales(disco);
                String detalleDisco = (index + 1) + ". " + disco.getNombreDisco() + ", " + disco.getArtista() +
                		", Precio: " + disco.getPrecio()+
                		", Ingresado por: " + disco.getIngresadoRetiradoPor();

                // Añadir campos opcionales a detalleDisco solo si existen
                if (!camposOpcionales.isEmpty()) {
                    detalleDisco += ", " + camposOpcionales;
                }
                
                IO.println(detalleDisco);
            }
        }
    }

    
    private static String obtenerCamposOpcionales(Disco disco) {
        StringBuilder campos = new StringBuilder();
        if (disco.getGenero() != null && !disco.getGenero().isEmpty()) {
            campos.append("Género: ").append(disco.getGenero()).append(", ");
        }
        if (disco.getFechaLanzamiento() != null) {
            campos.append("Fecha de Lanzamiento: ").append(disco.getFechaLanzamiento().toString()).append(", ");
        }
        if (disco.getFormato() != null && !disco.getFormato().isEmpty()) {
            campos.append("Formato: ").append(disco.getFormato()).append(", ");
        }
        if (disco.getDiscografica() != null && !disco.getDiscografica().isEmpty()) {
            campos.append("Discográfica: ").append(disco.getDiscografica()).append(", ");
        }

        // Elimina la última coma y espacio si hay campos
        if (campos.length() > 0) {
            campos.setLength(campos.length() - 2);
        }

        return campos.toString();
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
    
    private static void mostrarDetallesDisco(Disco disco) {
        IO.println("Detalles Actualizados del Disco:");
        IO.println("Nombre del Disco: " + disco.getNombreDisco());
        IO.println("Artista: " + disco.getArtista());
        IO.println("Precio: " + disco.getPrecio());
        IO.println("Ingresado por: " + disco.getIngresadoRetiradoPor());
        
        // Mostrar campos opcionales si están disponibles
        if (disco.getGenero() != null && !disco.getGenero().isEmpty()) {
            IO.println("Género: " + disco.getGenero());
        }
        if (disco.getFechaLanzamiento() != null) {
            IO.println("Fecha de Lanzamiento: " + disco.getFechaLanzamiento().toString());
        }
        if (disco.getFormato() != null && !disco.getFormato().isEmpty()) {
            IO.println("Formato: " + disco.getFormato());
        }
        if (disco.getDiscografica() != null && !disco.getDiscografica().isEmpty()) {
            IO.println("Discográfica: " + disco.getDiscografica());
        }
        // Agrega aquí más campos opcionales según cómo hayas definido tu clase Disco
    }




    private static void mostrarMenuDisco(Disco disco, DiscoDAO discoDAO) {
    	IO.println("Has seleccionado: " + disco.getNombreDisco());
        IO.println("1. Editar disco");
        IO.println("2. Retirar disco");
        IO.println("3. Borrar un campo opcional");
        IO.println("4. Volver al menú principal");

        int opcion = IO.readInt();

        switch (opcion) {
            case 1:
                editarDisco(discoDAO, disco);
                // Recuperar la versión actualizada del disco de la base de datos
                
                // Asumiendo que tienes un método para mostrar los detalles del disco
                
                break;
            case 2:
                IO.print("Ingrese su nombre de usuario para confirmar el retiro: ");
                String nombreUsuario = IO.readString();
                discoDAO.retirarDisco(disco.getNombreDisco(), nombreUsuario);
                break;
            case 3:
            	 borrarCampoOpcional(disco, discoDAO);
                 break;
            case 4:
                // No se necesita hacer nada aquí para volver al menú principal
                // La función terminará, y el bucle while en main se reanudará mostrando el menú principal nuevamente
                return;
            default:
                IO.println("Opción no válida, intente de nuevo.");
                break;
        }
    }




    private static void insertarDisco(DiscoDAO discoDAO) {
        String nombreDisco = IO.leerEntradaValida("Ingrese el nombre del disco:");
        String artista = IO.leerEntradaValida("Ingrese el artista:");
        // Para el precio, sigue usando readDouble ya que leerEntradaValida es para entradas de texto
        double precio = IO.leerPrecioValido("Ingrese el precio:");
        String ingresadoRetiradoPor = IO.leerEntradaValida("Ingresado por:");

        Disco.DiscoBuilder builder = Disco.builder()
                .nombreDisco(nombreDisco)
                .artista(artista)
                .precio(precio)
                .ingresadoRetiradoPor(ingresadoRetiradoPor);

        String respuesta="s";
        do {
            mostrarMenuAtributosOpcionales();
            int opcion = IO.readInt();
            switch (opcion) {
                case 1:
                    builder.genero(IO.leerEntradaValida("Ingrese el género:"));
                    break;
                case 2:
                    builder.fechaLanzamiento(IO.leerEntradaValida("Ingrese la fecha de lanzamiento (formato YYYY-MM-DD):"));
                    break;
                case 3:
                    builder.formato(IO.leerEntradaValida("Ingrese el formato:"));
                    break;
                case 4:
                    builder.discografica(IO.leerEntradaValida("Ingrese la discográfica:"));
                    break;
                case 5:
                    // Salir del bucle
                    respuesta = "n";
                    continue;
                default:
                    IO.println("Opción no válida, intente de nuevo.");
                    respuesta = "s"; // Asegúrate de pedir la entrada nuevamente si la opción no es válida
                    break;
            }

            // Esta pregunta se hace después de procesar la opción, pero antes de continuar el bucle.
            if (!"n".equals(respuesta)) {
                respuesta = IO.leerEntradaValida("¿Desea añadir más información? (s/n):").toLowerCase();
            }

        } while (!"n".equals(respuesta));

        Disco disco = builder.build();
        discoDAO.insertarDisco(disco);
        IO.println("Disco insertado con éxito.");
    }

    
    private static void borrarCampoOpcional(Disco disco, DiscoDAO discoDAO) {
        IO.println("Seleccione el campo opcional que desea borrar:");
        IO.println("1. Género");
        IO.println("2. Fecha de Lanzamiento");
        IO.println("3. Formato");
        IO.println("4. Discográfica");
        IO.println("5. Cancelar");
        
        int seleccion = IO.readInt();

        String campoABorrar = null;
        switch (seleccion) {
            case 1: campoABorrar = "genero"; break;
            case 2: campoABorrar = "fechaLanzamiento"; break;
            case 3: campoABorrar = "formato"; break;
            case 4: campoABorrar = "discografica"; break;
            case 5: return; // Cancelar operación
            default: IO.println("Selección no válida."); return;
        }

        discoDAO.borrarCampoOpcional(disco.getId(), campoABorrar);
        IO.println("Campo borrado exitosamente.");
    }

    
    private static void editarDisco(DiscoDAO discoDAO, Disco disco) {
        boolean seguirEditando = true;
        while (seguirEditando) {
            IO.println("¿Qué campo le gustaría editar?");
            IO.println("1. Nombre del Disco");
            IO.println("2. Artista");
            IO.println("3. Precio");
            IO.println("4. Ingresado por");
            IO.println("5. Género");
            IO.println("6. Fecha de Lanzamiento");
            IO.println("7. Formato");
            IO.println("8. Discográfica");
            IO.println("9. Finalizar Edición");

            int opcion = IO.readInt();
            switch (opcion) {
                case 1:
                    disco.setNombreDisco(IO.leerEntradaValida("Ingrese el nuevo nombre del disco:"));
                    break;
                case 2:
                    disco.setArtista(IO.leerEntradaValida("Ingrese el nuevo artista:"));
                    break;
                case 3:
                	double precio = IO.leerPrecioValido("Ingrese el nuevo precio:");
                    disco.setPrecio(precio);
                    break;
                case 4:
                    disco.setIngresadoRetiradoPor(IO.leerEntradaValida("Ingresado por:"));
                    break;
                case 5:
                    disco.setGenero(IO.leerEntradaValida("Ingrese el nuevo género:"));
                    break;
                case 6:
                    disco.setFechaLanzamiento(IO.leerEntradaValida("Ingrese la nueva fecha de lanzamiento (formato YYYY-MM-DD):"));
                    break;
                case 7:
                    disco.setFormato(IO.leerEntradaValida("Ingrese el nuevo formato:"));
                    break;
                case 8:
                    disco.setDiscografica(IO.leerEntradaValida("Ingrese la nueva discográfica:"));
                    break;
                case 9:
                    seguirEditando = false;
                    break;
                default:
                    IO.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
        discoDAO.actualizarDisco(disco);
        Disco discoActualizado = discoDAO.obtenerDiscoPorId(disco.getId());
        mostrarDetallesDisco(discoActualizado); // Mostrar los detalles actualizados
        IO.println("Disco actualizado con éxito.");
    }




    
    private static void mostrarMenuAtributosOpcionales() {
        IO.println("Seleccione el atributo que desea añadir:");
        IO.println("1. Género");
        IO.println("2. Fecha de Lanzamiento (formato YYYY-MM-DD)");
        IO.println("3. Formato");
        IO.println("4. Discográfica");
        IO.println("5. Finalizar y guardar");
    }
    
    //Metodos correspondientes a la búsqueda con filtros
    
    private static void mostrarMenuBusqueda(DiscoDAO discoDAO) {
        Map<Integer, String> opcionesFiltro = new HashMap<>();
        opcionesFiltro.put(1, "Nombre del Disco");
        opcionesFiltro.put(2, "Artista");
        opcionesFiltro.put(3, "Precio");
        opcionesFiltro.put(4, "Ingresado por");
        opcionesFiltro.put(5, "Género");
        opcionesFiltro.put(6, "Fecha de Lanzamiento");
        opcionesFiltro.put(7, "Formato");
        opcionesFiltro.put(8, "Discográfica");
        opcionesFiltro.put(9, "Finalizar y comenzar búsqueda");

        Map<String, String> filtrosAplicados = new HashMap<>();
        
        // Mapeo de nombres de filtros a nombres de campos de MongoDB
        Map<String, String> nombreCampoMongoDB = new HashMap<>();
        nombreCampoMongoDB.put("Nombre del Disco", "nombreDisco");
        nombreCampoMongoDB.put("Artista", "artista");
        nombreCampoMongoDB.put("Precio", "precio");
        nombreCampoMongoDB.put("Ingresado por", "ingresadoRetiradoPor");
        nombreCampoMongoDB.put("Género", "genero");
        nombreCampoMongoDB.put("Fecha de Lanzamiento", "fechaLanzamiento");
        nombreCampoMongoDB.put("Formato", "formato");
        nombreCampoMongoDB.put("Discográfica", "discografica");

        boolean seguirFiltrando = true;
        while (seguirFiltrando) {
            IO.println("¿Qué filtro deseas aplicar para la búsqueda?");
            opcionesFiltro.forEach((k, v) -> IO.println(k + ". " + v));
            int opcion = IO.readInt();
            
            if (opcion == 9) {
                seguirFiltrando = false;
                continue;
            }

            String campoSeleccionado = opcionesFiltro.get(opcion);
            if (campoSeleccionado != null && !campoSeleccionado.equals("Finalizar y comenzar búsqueda")) {
                String valorFiltro = IO.leerEntradaValida("Ingrese el valor para " + campoSeleccionado + ":");
                // Utiliza el mapeo para obtener el nombre del campo en MongoDB
                String campoMongoDB = nombreCampoMongoDB.get(campoSeleccionado);
                if (campoMongoDB != null) {
                    filtrosAplicados.put(campoMongoDB, valorFiltro);
                }
            } else {
                IO.println("Opción no válida, intente de nuevo.");
            }
        }

        if (!filtrosAplicados.isEmpty()) {
            buscarYMostrarResultados(discoDAO, filtrosAplicados);
        } else {
            IO.println("No se aplicaron filtros. Volviendo al menú principal.");
        }
    }

    
    private static void buscarYMostrarResultados(DiscoDAO discoDAO, Map<String, String> filtros) {
        List<Disco> resultados = discoDAO.buscarDiscosConFiltros(filtros);
        if (resultados.isEmpty()) {
            IO.println("No se encontraron discos con los filtros aplicados.");
        } else {
            IO.println("Resultados de la búsqueda:");
            for (int i = 0; i < resultados.size(); i++) {
                Disco disco = resultados.get(i);
                IO.println((i + 1) + ". " + obtenerDetallesDiscoEnLinea(disco));
            }
            seleccionarYMostrarMenuDisco(resultados, discoDAO);
        }
    }

    
    private static void mostrarDetallesDisco2(Disco disco) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Nombre del Disco: ").append(disco.getNombreDisco());
        detalles.append(", Artista: ").append(disco.getArtista());
        detalles.append(", Precio: ").append(disco.getPrecio());
        detalles.append(", Ingresado/Retirado por: ").append(disco.getIngresadoRetiradoPor());

        // Añade los campos opcionales solo si están presentes
        if (disco.getGenero() != null && !disco.getGenero().isEmpty()) {
            detalles.append(", Género: ").append(disco.getGenero());
        }
        if (disco.getFechaLanzamiento() != null) {
            detalles.append(", Fecha de Lanzamiento: ").append(disco.getFechaLanzamiento());
        }
        if (disco.getFormato() != null && !disco.getFormato().isEmpty()) {
            detalles.append(", Formato: ").append(disco.getFormato());
        }
        if (disco.getDiscografica() != null && !disco.getDiscografica().isEmpty()) {
            detalles.append(", Discográfica: ").append(disco.getDiscografica());
        }
        
        // Finalmente, imprime la cadena completa de detalles
        IO.println(detalles.toString());
    }
    
    private static void seleccionarYMostrarMenuDisco(List<Disco> discos, DiscoDAO discoDAO) {
        IO.println("Seleccione el número del disco para ver más opciones o 0 para volver:");
        int seleccion = IO.readInt() - 1;
        
        if (seleccion >= 0 && seleccion < discos.size()) {
            Disco discoSeleccionado = discos.get(seleccion);
            mostrarMenuDisco(discoSeleccionado, discoDAO);
        } else if (seleccion != -1) {
            IO.println("Selección no válida.");
        }
    }
    
    private static String obtenerDetallesDiscoEnLinea(Disco disco) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Nombre del Disco: ").append(disco.getNombreDisco());
        detalles.append(", Artista: ").append(disco.getArtista());
        detalles.append(", Precio: ").append(disco.getPrecio());
        detalles.append(", Ingresado/Retirado por: ").append(disco.getIngresadoRetiradoPor());

        // Añade los campos opcionales solo si están presentes
        if (disco.getGenero() != null && !disco.getGenero().isEmpty()) {
            detalles.append(", Género: ").append(disco.getGenero());
        }
        if (disco.getFechaLanzamiento() != null) {
            detalles.append(", Fecha de Lanzamiento: ").append(disco.getFechaLanzamiento());
        }
        if (disco.getFormato() != null && !disco.getFormato().isEmpty()) {
            detalles.append(", Formato: ").append(disco.getFormato());
        }
        if (disco.getDiscografica() != null && !disco.getDiscografica().isEmpty()) {
            detalles.append(", Discográfica: ").append(disco.getDiscografica());
        }

        return detalles.toString();
    }

    
    




}
