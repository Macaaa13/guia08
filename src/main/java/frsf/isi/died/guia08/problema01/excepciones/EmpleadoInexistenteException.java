package frsf.isi.died.guia08.problema01.excepciones;

public class EmpleadoInexistenteException extends Exception {

	public EmpleadoInexistenteException(String s) {
		super("Error: " + s);
	}
	
}
