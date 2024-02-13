package dao;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import io.IO;
import modelo.Disco;


import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import util.MongoDBUtil; 

public class DiscoDAO {
    private MongoDatabase database;

    public DiscoDAO() {
        
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

        
        if (doc.getObjectId("_id") != null) {
            builder.id(doc.getObjectId("_id").toString());
        }

        
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
        
        MongoCollection<Document> collection = database.getCollection("discos");

        
        Document doc = new Document("nombreDisco", disco.getNombreDisco())
                .append("artista", disco.getArtista())
                .append("precio", disco.getPrecio())
                .append("ingresadoRetiradoPor", disco.getIngresadoRetiradoPor());

       
        if (disco.getGenero() != null) {
            doc.append("genero", disco.getGenero());
        }
        
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

        
        datosActualizados.append("nombreDisco", disco.getNombreDisco())
                         .append("artista", disco.getArtista())
                         .append("precio", disco.getPrecio())
                         .append("ingresadoRetiradoPor", disco.getIngresadoRetiradoPor());

        
        if (disco.getGenero() != null && !disco.getGenero().isEmpty()) {
            datosActualizados.append("genero", disco.getGenero());
        }
        if (disco.getFechaLanzamiento() != null) {
            
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
       
        Document found = collection.findOneAndDelete(new Document("nombreDisco", nombreDisco));
        if (found != null) {
           
            System.out.println("Disco retirado con éxito.");
        } else {
            System.out.println("Disco no encontrado.");
        }
    }
    
    
    public List<Disco> buscarDiscosConFiltros(Map<String, String> filtros) {
        List<Disco> discos = new ArrayList<>();
        List<Bson> condiciones = new ArrayList<>();

        filtros.forEach((campo, valor) -> {
            if ("precio".equals(campo)) {
                try {
                    double precioVal = Double.parseDouble(valor);
                    condiciones.add(Filters.eq(campo, precioVal));
                } catch (NumberFormatException e) {
                    IO.println("Error al convertir el precio: " + valor);
                }
            } else if (campo.startsWith("precio>")) {
                try {
                    double precioVal = Double.parseDouble(valor);
                    condiciones.add(Filters.gt("precio", precioVal));
                } catch (NumberFormatException e) {
                    IO.println("Error al interpretar el precio para el filtro >: " + valor);
                }
            } else if (campo.startsWith("precio<")) {
                try {
                    double precioVal = Double.parseDouble(valor);
                    condiciones.add(Filters.lt("precio", precioVal));
                } catch (NumberFormatException e) {
                    IO.println("Error al interpretar el precio para el filtro <: " + valor);
                }
            } else {
                condiciones.add(Filters.regex(campo, ".*" + Pattern.quote(valor) + ".*", "i"));
            }
        });

        if (!condiciones.isEmpty()) {
            MongoCollection<Document> collection = database.getCollection("discos");
            for (Document doc : collection.find(Filters.and(condiciones))) {
                discos.add(documentADisco(doc));
            }
        }

        return discos;
    }


}
