package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import io.IO;
import modelo.Disco;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import util.MongoDBUtil; // Asegúrate de importar correctamente MongoDBUtil

public class DiscoDAO {
    private MongoDatabase database;

    public DiscoDAO() {
        // Utiliza MongoDBUtil para obtener la base de datos
        this.database = MongoDBUtil.getDatabase();
    }
    
    public List<Disco> obtenerTodosLosDiscos() {
        List<Disco> discos = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("discos");

        for (Document doc : collection.find()) {
            discos.add(documentADisco(doc));
        }

        return discos;
    }

    
    public Disco obtenerDiscoPorId(String id) {
        MongoCollection<Document> collection = database.getCollection("discos");
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentADisco(doc) : null;
    }



    private Disco documentADisco(Document doc) {
        Disco.DiscoBuilder builder = Disco.builder()
                .nombreDisco(doc.getString("nombreDisco"))
                .artista(doc.getString("artista"))
                .precio(doc.getDouble("precio"))
                .ingresadoRetiradoPor(doc.getString("ingresadoRetiradoPor"));

        // Incluir el _id del documento como el id del objeto Disco
        if (doc.getObjectId("_id") != null) {
            builder.id(doc.getObjectId("_id").toString());
        }

        // Manejar campos opcionales
        if (doc.containsKey("genero") && doc.getString("genero") != null) {
            builder.genero(doc.getString("genero"));
        }
        if (doc.containsKey("fechaLanzamiento")) {
            builder.fechaLanzamiento(doc.getString("fechaLanzamiento"));
        }

        if (doc.containsKey("formato") && doc.getString("formato") != null) {
            builder.formato(doc.getString("formato"));
        }
        if (doc.containsKey("discografica") && doc.getString("discografica") != null) {
            builder.discografica(doc.getString("discografica"));
        }

        return builder.build();
    }




    public void insertarDisco(Disco disco) {
        // Obtén la colección 'discos' de la base de datos
        MongoCollection<Document> collection = database.getCollection("discos");

        // Crea un nuevo documento a partir de los datos del disco
        Document doc = new Document("nombreDisco", disco.getNombreDisco())
                .append("artista", disco.getArtista())
                .append("precio", disco.getPrecio())
                .append("ingresadoRetiradoPor", disco.getIngresadoRetiradoPor());

        // Añade campos opcionales solo si están presentes
        if (disco.getGenero() != null) {
            doc.append("genero", disco.getGenero());
        }
        // Repite para otros campos opcionales
        
        // Inserta el documento en la colección
        collection.insertOne(doc);
    }
    
    public void borrarCampoOpcional(String discoId, String campoABorrar) {
        Document filtro = new Document("_id", new ObjectId(discoId));
        Document actualizacion = new Document("$unset", new Document(campoABorrar, ""));

        MongoCollection<Document> collection = database.getCollection("discos");
        collection.updateOne(filtro, actualizacion);
    }
    
   
    
    public void actualizarDisco(Disco disco) {
        Document filtro = new Document("_id", new ObjectId(disco.getId()));
        Document datosActualizados = new Document();

        // Añadir campos que siempre están presentes
        datosActualizados.append("nombreDisco", disco.getNombreDisco())
                         .append("artista", disco.getArtista())
                         .append("precio", disco.getPrecio())
                         .append("ingresadoRetiradoPor", disco.getIngresadoRetiradoPor());

        // Añadir condicionalmente campos opcionales
        if (disco.getGenero() != null && !disco.getGenero().isEmpty()) {
            datosActualizados.append("genero", disco.getGenero());
        }
        if (disco.getFechaLanzamiento() != null) {
            // Asegúrate de formatear la fecha correctamente según tu diseño
            datosActualizados.append("fechaLanzamiento", disco.getFechaLanzamiento());
        }
        if (disco.getFormato() != null && !disco.getFormato().isEmpty()) {
            datosActualizados.append("formato", disco.getFormato());
        }
        if (disco.getDiscografica() != null && !disco.getDiscografica().isEmpty()) {
            datosActualizados.append("discografica", disco.getDiscografica());
        }

        MongoCollection<Document> collection = database.getCollection("discos");
        collection.updateOne(filtro, new Document("$set", datosActualizados));
    }





    
    public void retirarDisco(String nombreDisco, String nombreUsuario) {
        MongoCollection<Document> collection = database.getCollection("discos");
        // Asume que 'nombreDisco' es único; ajusta según sea necesario
        Document found = collection.findOneAndDelete(new Document("nombreDisco", nombreDisco));
        if (found != null) {
            // Aquí podrías incluir lógica para registrar el retiro, usando 'nombreUsuario'
            System.out.println("Disco retirado con éxito.");
        } else {
            System.out.println("Disco no encontrado.");
        }
    }
    
    
    public List<Disco> buscarDiscosConFiltros(Map<String, String> filtros) {
        List<Disco> discos = new ArrayList<>();
        List<Bson> condiciones = new ArrayList<>();

        for (Map.Entry<String, String> filtro : filtros.entrySet()) {
            String key = filtro.getKey();
            String value = filtro.getValue();

            if ("precio".equals(key)) {
                try {
                    double precio = Double.parseDouble(value);
                    condiciones.add(Filters.eq(key, precio));
                } catch (NumberFormatException e) {
                    System.out.println("Error en el formato del precio: " + value);
                }
            } else {
                condiciones.add(Filters.regex(key, Pattern.quote(value), "i"));
            }
        }

        Bson consultaFinal = condiciones.isEmpty() ? new Document() : Filters.and(condiciones);

        MongoCollection<Document> collection = database.getCollection("discos");
        for (Document doc : collection.find(consultaFinal)) {
            discos.add(documentADisco(doc)); // Asegúrate de tener este método implementado correctamente
        }

        return discos;
    }



    

    // Implementa aquí otros métodos CRUD
}
