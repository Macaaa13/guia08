package frsf.isi.died.guia08.problema01.excepciones;

public class TareaInexistenteException extends Exception {

	public TareaInexistenteException(String s) {
		super("Error: " + s);
	}
}
