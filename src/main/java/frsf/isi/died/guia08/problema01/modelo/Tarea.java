package frsf.isi.died.guia08.problema01.modelo;

import java.time.*;
import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;

public class Tarea {

	//--- Atributos ---
	private Integer id;
	private String descripcion;
	private Integer duracionEstimada;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Boolean facturada;
	
	
	//--- Constructores ---
	public Tarea(Integer id, String desc, Integer durac) {
		this.id = id;
		descripcion = desc;
		duracionEstimada = durac;
		facturada = false;
	}

	
	//--- Getters y Setters ---
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getFacturada() {
		return facturada;
	}

	public void setFacturada(Boolean facturada) {
		this.facturada = facturada;
	}

	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}
	
	public void setEmpleadoAsignado(Empleado empleadoAsignado) {
		this.empleadoAsignado = empleadoAsignado;
	}


	//--- Métodos ---
	//-------------------------
	//----- Ejercicio 2.b -----
	//-------------------------
	/** Se estiman 4 horas de trabajo por día, por lo que se múltiplica por 4 la diferencia en días entre la fecha de inicio y la final, 
	 *  obteniendo la duración real de horas de trabajo.
	 *  Se suma 1 a la diferencia entre fechas inicial y final ya que una tarea pudo haberse hecho en menos de 1 día, pero aún así se considera
	 *  que fueron 4 horas. Además, si una tarea se inicia y finaliza en el mismo día se considerarían 0 horas y no 4 como deberían.
	 *  Si la diferencia entre la duración estimada y la real es mayor a 0 significa que la tarea fue terminada antes de lo estimado.		 */
	public Boolean tareaAdelantada() {
		if(this.getDuracionEstimada()-( ( (Duration.between(fechaInicio, fechaFin).toDays())+1 )*4 )>0) {
			return true;
		}
		return false;
	}
		
	/** Si un empleado contratado se demora más de 2 días en finalizar la tarea respecto a lo que estaba estimado, la diferencia entre
	 *  la duración estimada y la real es menor que -8, ya que para cada día de trabajo se estiman 4 horas.
	 */
	public Boolean tareaAtrasada() {
		if((this.getDuracionEstimada()-( (Duration.between(fechaInicio, fechaFin).toDays()+1)*4)) <-8) {
			return true;
		}
		return false;
	}
	
	//-------------------------
	//----- Ejercicio 3 -----
	//-------------------------
	/** Verifica si al empleado se le puede asignar la tarea. La funcion asignarTarea de Empleado se encarga de verificar que la tarea
	 *  no tenga ya un empleado asignado ni haya sido finalizada, lanzando una AsignacionIncorrectaException con el mensaje correspondiente
	 *  en cada caso.
	 *  Si se puede asignar le setea el empleado a la tarea y la tarea es agregada a la lista de tareas asignadas del empleado.
	 *  Si no se puede asignar porque el empleado no cumple las condiciones necesarias se lanza una AsignacionIncorrectaException.
	 */
	public void asignarEmpleado(Empleado e) throws AsignacionIncorrectaException {
		// si la tarea ya tiene un empleado asignado
		// y tiene fecha de finalizado debe lanzar una excepcion
		if(e.asignarTarea(this)) {
			this.setEmpleadoAsignado(e);
			e.getTareasAsignadas().add(this);
		}
		else {
			throw new AsignacionIncorrectaException("El empleado no puede ser asignado porque no cumple las condiciones necesarias.");
		}
	}

	//-------------------------
	//----- Ejercicio 4.c -----
	//-------------------------
	@Override
	public boolean equals(Object o) {
		Tarea t = (Tarea) o;
		return(this.id.equals(t.getId()) &&
			   this.descripcion.equals(t.getDescripcion()) &&
			   this.duracionEstimada.equals(t.getDuracionEstimada()));
	}
	
	@Override
	public String toString() {
		return id+", "+descripcion+", "+duracionEstimada;
	}
	
	//-------------------------
	//----- Ejercicio 4.i -----
	//-------------------------
	public String asCsv() {
		return id+";"+descripcion+";"+duracionEstimada;
	}
}
