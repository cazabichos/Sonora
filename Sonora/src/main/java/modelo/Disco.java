package modelo;

import lombok.Builder;
import lombok.Data;




@Data 
@Builder 
public class Disco {
	private String id;
    private String nombreDisco; // Obligatorio
    private String artista; // Obligatorio
    private double precio; // Obligatorio
    private String ingresadoRetiradoPor; // Obligatorio
    private String genero; // Opcional
    private String fechaLanzamiento; // Opcional
    private String formato; // Opcional
    private String discografica; // Opcional
}
