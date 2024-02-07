package io;

import java.util.Scanner;

/**
 * <p>
 * Clase estática para leer de teclado con comprobación de tipo de datos y
 * escribir en pantalla.
 * </p>
 * 
 * <p>
 * <b>USO EDUCATIVO</b>
 * </p>
 * 
 * <p>
 * Los tipos de dato que maneja son:
 * </p>
 * 
 * <ul>
 * <li>entero (int)
 * <li>decimal (double)
 * <li>caracter (char)
 * <li>byte
 * <li>short
 * <li>int
 * <li>long
 * <li>float
 * <li>double
 * <li>boolean (true, false)
 * <li>char
 * <li>String (admite tira vacía)
 * </ul>
 * 
 * @author Amadeo
 * @version 1.0
 * @since 2018-07-01
 */
public class IO {

	private static Scanner sc = new Scanner(System.in);

	/**
	 * Constructor
	 */
	IO() {
		sc.useDelimiter("\n");
	}

	/**
	 * Muestra un objeto
	 * 
	 * @param o
	 *            objeto
	 */
	static public void print(Object o) {
		System.out.print(o);
	}

	/**
	 * Muestra un objeto y salta de l�nea
	 * 
	 * @param o
	 *            objeto
	 */
	static public void println(Object o) {
		System.out.println(o);
	}

	/**
	 * Lee un valor de tipo byte
	 * 
	 * @return
	 */
	static public byte readByte() {
		while (true) {
			try {
				return Byte.parseByte(sc.nextLine());
			} catch (Exception e) {
				System.err.print("ERROR: No es de tipo byte ? ");
			}
		}
	}

	/**
	 * Lee un valor de tipo short
	 * 
	 * @return
	 */
	static public short readShort() {
		while (true) {
			try {
				return Short.parseShort(sc.nextLine());
			} catch (Exception e) {
				System.err.print("ERROR: No es de tipo short ? ");
			}
		}
	}
	
	 public static String leerEntradaValida(String mensaje) {
	        String entrada;
	        do {
	            IO.println(mensaje); // Usa IO.println para mantener la consistencia con el resto de tu clase
	            entrada = sc.nextLine().trim(); // Elimina espacios al principio y al final
	            if (entrada.isEmpty()) {
	                IO.println("La entrada no puede estar vacía ni consistir solo de espacios en blanco. Por favor, intente de nuevo.");
	            }
	        } while (entrada.isEmpty());
	        return entrada;
	    }
	 
	 public static double leerPrecioValido(String mensaje) {
	        double valor = 0;
	        boolean entradaValida = false;
	        do {
	            System.out.println(mensaje); // Usa System.out.println directamente o adapta según cómo esté estructurada tu clase IO.
	            String entrada = sc.nextLine().trim(); // Elimina espacios al principio y al final
	            if (entrada.isEmpty()) {
	                System.out.println("La entrada no puede estar vacía ni consistir solo de espacios en blanco. Por favor, intente de nuevo.");
	            } else {
	                try {
	                    valor = Double.parseDouble(entrada);
	                    entradaValida = true; // Si llegamos aquí, la entrada es válida
	                } catch (NumberFormatException e) {
	                    System.out.println("Entrada no válida. Por favor, ingrese un número válido.");
	                }
	            }
	        } while (!entradaValida);
	        return valor;
	    }

	/**
	 * Lee un valor de tipo int
	 * 
	 * @return
	 */
	static public int readInt() {
		while (true) {
			try {
				return Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				System.err.print("ERROR: No es de tipo int ? ");
			}
		}
	}

	/**
	 * Lee un valor de tipo long
	 * 
	 * @return
	 */
	static public long readLong() {
		while (true) {
			try {
				return Long.parseLong(sc.nextLine());
			} catch (Exception e) {
				System.err.print("ERROR: No es de tipo long ? ");
			}
		}
	}

	/**
	 * Lee un valor de tipo float
	 * 
	 * @return
	 */
	static public float readFloat() {
		while (true) {
			try {
				return Float.parseFloat(sc.nextLine());
			} catch (Exception e) {
				System.err.print("ERROR: No es de tipo float ? ");
			}
		}
	}

	/**
	 * Lee un valor de tipo double
	 * 
	 * @return
	 */
	static public double readDouble() {
		while (true) {
			try {
				return Double.parseDouble(sc.nextLine());
			} catch (Exception e) {
				System.err.print("ERROR: No es de tipo double ? ");
			}
		}
	}

	/**
	 * Lee un valor de tipo boolean
	 * 
	 * @return
	 */
	static public boolean readBoolean() {
		while (true) {
			String s = sc.nextLine();
			if (s.equals("true")) return true;
			if (s.equals("false")) return false;
			System.err.print("ERROR: No es de tipo boolean (true o false) ? ");
		}
	}

	/**
	 * Lee un valor de tipo char
	 * 
	 * @return
	 */
	static public char readChar() {
		while (true) {
			String s = sc.nextLine();
			if (s.length() == 1) {
				return s.toCharArray()[0];
			}
			System.err.print("ERROR: No es de tipo char ? ");
		}
	}

	/**
	 * Lee un valor de tipo String
	 * 
	 * @return
	 */
	static public String readString() {
		return sc.nextLine();
	}

	/*
	 * Métodos para idioma español
	 *
	 * static public void escribir(Object o) { println(o); }
	 * static public void mostrar(Object o) { println(o); }
	 * static public int leerEntero() { return readInt(); }
	 * static public double leerDecimal() { return readDouble(); }
	 * static public char leerCaracter() { return readChar(); }
	 */

}
