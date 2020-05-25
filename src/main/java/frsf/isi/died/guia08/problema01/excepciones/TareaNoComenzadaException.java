package frsf.isi.died.guia08.problema01.excepciones;

public class TareaNoComenzadaException extends Exception {

	public TareaNoComenzadaException() {
		super("Error: No puede finalizar una tarea que no ha comenzado.");
	}
	
}
