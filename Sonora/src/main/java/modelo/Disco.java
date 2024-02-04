package modelo;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data // Genera getters, setters, toString, equals y hashCode
@Builder // Genera un constructor builder para la creación de instancias
public class Disco {
    private String nombreDisco; // Obligatorio
    private String artista; // Obligatorio
    private double precio; // Obligatorio
    private String ingresadoRetiradoPor; // Obligatorio
    private String genero; // Opcional
    private Date fechaLanzamiento; // Opcional
    private String formato; // Opcional
    private String discografica; // Opcional
}
