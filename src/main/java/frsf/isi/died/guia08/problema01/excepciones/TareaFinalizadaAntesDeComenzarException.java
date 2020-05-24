package frsf.isi.died.guia08.problema01.excepciones;

public class TareaFinalizadaAntesDeComenzarException extends Exception {

	public TareaFinalizadaAntesDeComenzarException() {
		super("Error: la fecha de finalización de la tarea no puede ser posterior a la de inicialización.");
	}
	
}
