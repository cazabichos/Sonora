package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import modelo.Disco;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
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

        // Recupera todos los documentos de la colección
        for (Document doc : collection.find()) {
            discos.add(documentADisco(doc));
        }

        return discos;
    }

    private Disco documentADisco(Document doc) {
        // Inicia la construcción del objeto Disco
        Disco.DiscoBuilder builder = Disco.builder()
                .nombreDisco(doc.getString("nombreDisco"))
                .artista(doc.getString("artista"))
                .precio(doc.getDouble("precio"))
                .ingresadoRetiradoPor(doc.getString("ingresadoRetiradoPor"));
        
        // Comprobación y asignación de campos opcionales
        if (doc.containsKey("genero")) {
            builder.genero(doc.getString("genero"));
        }
        // Añade aquí más comprobaciones para otros campos opcionales

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
    
    public void actualizarDisco(Disco disco) {
        Document filtro = new Document("nombreDisco", disco.getNombreDisco());
        Document datosActualizados = new Document("$set", new Document()
            .append("artista", disco.getArtista())
            .append("precio", disco.getPrecio())
            .append("ingresadoRetiradoPor", disco.getIngresadoRetiradoPor())
            .append("genero", disco.getGenero())
            // Añade el resto de campos según tu modelo
        );
        MongoCollection<Document> collection = database.getCollection("discos");
        collection.updateOne(filtro, datosActualizados);
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

    // Implementa aquí otros métodos CRUD
}
