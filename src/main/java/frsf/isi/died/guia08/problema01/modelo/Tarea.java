package frsf.isi.died.guia08.problema01.modelo;

import java.time.Duration;
import java.time.LocalDateTime;

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
	public void asignarEmpleado(Empleado e) {
		// si la tarea ya tiene un empleado asignado
		// y tiene fecha de finalizado debe lanzar una excepcion
	}
	
	//Ejercicio 2.b
	/** Se estiman 4 horas de trabajo por día, por lo que se múltiplica por 4
	 *	la diferencia en días entre la fecha de inicio y la final, obteniendo las horas
	 *  trabajadas por días.
	 *  Si la diferencia entre la duración estimada y la real es mayor a 0, significa
	 *  que la tarea fue terminada antes de lo estimado.
	 */
	public Boolean tareaAdelantada() {
		if(this.getDuracionEstimada()-(Duration.between(fechaInicio, fechaFin).toDays()*4)>0) {
			return true;
		}
		return false;
	}
	
	/** Si un empleado contratado se demora más de 2 días en finalizar la tarea respecto a lo
	 *  que estaba estimado, la diferencia entre la duración estimada y la real es menor que -8,
	 *  ya que para cada día de trabajo se estiman 4 horas.
	 */
	public Boolean tareaAtrasada() {
		if((this.getDuracionEstimada()-(Duration.between(fechaInicio, fechaFin).toDays()*4))<-8) {
			return true;
		}
		return false;
	}

	
}
