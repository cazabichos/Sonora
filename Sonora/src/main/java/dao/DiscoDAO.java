package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

    // Implementa aquí otros métodos CRUD
}
