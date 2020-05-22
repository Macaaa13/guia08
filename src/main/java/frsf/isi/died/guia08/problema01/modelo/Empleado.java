package frsf.isi.died.guia08.problema01.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;

public class Empleado {

	//--- Atributos ---
	public enum Tipo { CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	
	private Function<Tarea, Double> calculoPagoPorTarea;
	private Predicate<Tarea> puedeAsignarTarea;

	
	//--- Constructores ---
	public Empleado(Integer c, String n, Tipo t, Double costo) {
		cuil = c;
		nombre = n;
		tipo = t;
		costoHora = costo;
		tareasAsignadas = new ArrayList<Tarea>();
		switch(t) {
		case CONTRATADO:
			puedeAsignarTarea = tarea -> tarea.getEmpleadoAsignado().getTareasAsignadas().size()<=5;
			break;
		case EFECTIVO:
			puedeAsignarTarea = tarea -> tarea.getEmpleadoAsignado().efectivoTareasPendientes()<=15;
			break;
		}
	}
	
	//Getters y Setters
	public Integer getCuil() {
		return cuil;
	}

	public void setCuil(Integer cuil) {
		this.cuil = cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Double getCostoHora() {
		return costoHora;
	}

	public void setCostoHora(Double costoHora) {
		this.costoHora = costoHora;
	}

	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}
	
	
	//--- Métodos ---
	
	//----- Ejercicio 2.a -----
	/** - Empleados contratados -> No pueden tener más de 5 tareas asignadas pendientes
	 *  - Empleados efectivos -> No pueden tener tareas asignadas pendientes que sumen
	 *  más de 15 horas de trabajo estimado
	 *  - En caso de asignar un empleado a una tarea que ya tiene otro empleado asignado
	 *  o a una tarea que ya fue finalizada, se lanza una excepción
	 */
	public Boolean asignarTarea(Tarea t) throws AsignacionIncorrectaException {
		if(t.getEmpleadoAsignado() != this || t.getFechaFin() != null) {
			throw new AsignacionIncorrectaException();
		}
		return puedeAsignarTarea.test(t);
	}

	/** Comprueba que las tareas pendientes del empleado no sumen más de 15 horas
	 */
	public Integer efectivoTareasPendientes() {
		return tareasAsignadas.stream().mapToInt(t -> t.getDuracionEstimada()).sum();
	}
	
	//----- Ejercicio 2.b -----
	public Double salario() {
		// cargar todas las tareas no facturadas
		// calcular el costo
		// marcarlas como facturadas.
		
		List<Tarea> lista = tareasAsignadas.stream()
				   .filter(t -> t.getFacturada() == false)
				   .collect(Collectors.toList());

		Double s = 0.0;
		for(Tarea t: lista) {
				s += this.costoTarea(t);
		}

		lista.stream().forEach(t -> t.setFacturada(true));

		return s;	
		
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		if(t.getFechaFin() != null) {
			
		}
		return 0.0;
	}
	
	public void comenzar(Integer idTarea) {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
	}
	
	public void finalizar(Integer idTarea) {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}

	public void comenzar(Integer idTarea,String fecha) {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}
	
	public void finalizar(Integer idTarea,String fecha) {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}
}
