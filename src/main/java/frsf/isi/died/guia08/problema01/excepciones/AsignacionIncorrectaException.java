package frsf.isi.died.guia08.problema01.excepciones;

public class AsignacionIncorrectaException extends Exception {

	public AsignacionIncorrectaException() {
		super("Error: debe seleccionar una tarea que no tenga un empleado asignado o que no haya sido finalizada");
	}

}
