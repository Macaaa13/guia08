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
	/*   Al ser 4 horas teóricas de trabajo diarias, se calculan los días entre la fecha de inicio
	 *   y final de la tarea y se multiplica por esas 4 horas.
	 */
	public long duracionTotal() {
		Duration d = Duration.between(fechaInicio, fechaFin);
		return (d.toDays())*4;
	}
	
	/** Los empleados cobran un extra si terminan la tarea antes de lo planificado.
	 *  Los empleados efectivos aumentan el valor de su hora en un 20% en aquellas
	 *  tareas que finalizaron antes.
	 */
	public Double calcularPotenciadorEfectivo() {
		if(getDuracionEstimada() - duracionTotal()>0) {
			return 1.2;
		}
		return 1.0;
	}
	
	
	/** Los empleados cobran un extra si terminan la tarea antes de lo planificado.
	 *  Los empleados contratados aumentan el valor de su hora en un 30% para estas 
	 *  tareas, y si demoran más de 2 días en finalizarla se cobrará un 75% de su 
	 *  valor hora.
	 */
	public Double calcularPotenciadorContratado() {
		long duracion = getDuracionEstimada() - duracionTotal();
		// Cada día tiene 4 horas de trabajo estimado.
		// Si supera las 8 horas de diferencia significa que tardó más de 2 días
		// en finalizar la tarea
		if(duracion<-8) {
			return 0.75;
		}
		else if(duracion>0) {
			return 1.3;
		}
		return 1.0;
	}

	
}
