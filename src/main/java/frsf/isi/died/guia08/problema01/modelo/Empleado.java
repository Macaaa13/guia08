package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.*;

import frsf.isi.died.guia08.problema01.excepciones.*;

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
	/** Los empleados efectivos no pueden tener tareas asignadas pendientes que sumen más de 15 horas de trabajo estimado.
	 *  Cobran la duración estimada de la tarea multiplicada por el costo hora, aumentando el valor de su hora en un 20% 
	 *  en aquellas tareas que hayan finalizado antes.
	 */
	public void configurarEfectivo() {
		puedeAsignarTarea = t -> ((this.efectivoTareasPendientes() + t.getDuracionEstimada())<=15);
		calculoPagoPorTarea = t -> {
			if(t.tareaAdelantada()) { return t.getDuracionEstimada()*this.costoHora*1.2;}
			return t.getDuracionEstimada()*this.costoHora;
		};
	}
	
	/** Los empleados contratados no pueden tener más de 5 tareas asignadas pendientes.
	 *  Cobran la duración estimada de la tarea multiplicada por el costo hora, aumentando el valor de su hora en un 30% 
	 *  en aquellas tareas que hayan terminado antes y cobrando un 75% de su valor hora si demoran más de 2 días en
	 *  finalizarla respecto a lo que estaba estimado.
	 */
	public void configurarContratado() {
		puedeAsignarTarea = t -> this.contratadoTareasPendientes()<5;
		calculoPagoPorTarea = t -> {
			if(t.tareaAdelantada()) { return t.getDuracionEstimada()*this.costoHora*1.3;}
			if(t.tareaAtrasada()) { return t.getDuracionEstimada()*this.costoHora*0.75; }
			return t.getDuracionEstimada()*this.costoHora;
		};	
	}
	
	//-------------------------
	//----- Ejercicio 2.a -----
	//-------------------------
	/** La excepción AsignacionIncorrectaException se lanza en caso de intentar asignar un empleado a una tarea que:
	 *  a) Ya tiene otro empleado asignado.
	 *  b) Ya fue finalizada.
	 *  Si no se lanza, se comprueba si el empleado cumple las condiciones necesarias para ser asignado a la tarea.
	 */
	public Boolean asignarTarea(Tarea t) throws AsignacionIncorrectaException {
		if(t.getEmpleadoAsignado() != null) {
			throw new AsignacionIncorrectaException("Debe seleccionar una tarea que no tenga un empleado asignado.");
		}
		if(t.getFechaFin() != null) {
			throw new AsignacionIncorrectaException("Debe seleccionar una tarea que no haya sido finalizada.");
		}
		return puedeAsignarTarea.test(t);
	}

	/** Retorna la suma de las horas estimadas de trabajo de las tareas que el empleado tiene asignadas.
	 */
	public Integer efectivoTareasPendientes() {
		return tareasAsignadas.stream().filter(t -> t.getFechaFin()==null).mapToInt(t -> t.getDuracionEstimada()).sum();
	}
	
	/** Retorna la cantidad de tareas pendientes a finalizar que el empleado tiene asignadas.
	 */
	public long contratadoTareasPendientes() {
		return tareasAsignadas.stream().filter(t -> t.getFechaFin()==null).count();
	}
	
	//-------------------------
	//----- Ejercicio 2.b -----
	//-------------------------
	/** Se filtran las tareas no facturadas ya finalizadas de la lista de tareas asignadas del empleado.
	 *  Luego se setean como facturadas y se calcula para cada una el costo, retornando la suma de estos costos. 
	 *  Se verifica que la tarea haya sido finalizada (lo cual implica, a su vez, que ésta haya sido comenzada) ya que en caso contrario
	 *  no se puede calcular el costo (éste depende de si el empleado terminó la tarea antes o después de lo estimado, lo cual no puede calcularse
	 *  si no se sabe cuánto tardo el empleado en finalizarla).
	 */
	public Double salario() {
		// cargar todas las tareas no facturadas
		// calcular el costo
		// marcarlas como facturadas.
		
		return tareasAsignadas.stream()
				   .filter(t -> t.getFacturada() == false && t.getFechaFin()!=null)
				   .map(t -> {
					   t.setFacturada(true);
					   return t;
				   })
				   .mapToDouble(t -> this.costoTarea(t))
				   .sum();
		
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuál es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 */
	public Double costoTarea(Tarea t) {
		return this.calculoPagoPorTarea.apply(t);
	}
	
	//-------------------------
	//----- Ejercicio 2.c -----
	//-------------------------
	/** Busca la tarea en la lista de tareas: si no existe lanza una TareaInexistenteException, si existe indica como fecha inicial la fecha
	 *  y hora actual.
	 */
	public void comenzar(Integer idTarea) throws TareaInexistenteException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
		Optional<Tarea> tarea = tareasAsignadas.stream().filter(t -> t.getId() == idTarea).findFirst();
		if(tarea.isEmpty()) {
			throw new TareaInexistenteException("La tarea que desea comenzar no se encuentra en la lista de tareas asignadas.");
		}
		else {
			tarea.get().setFechaInicio(LocalDateTime.now());
		}
	}
	
	//-------------------------
	//----- Ejercicio 2.d -----
	//-------------------------
	/** Busca la tarea en la lista de tareas asignadas: si no existe lanza una TareaInexistenteException, si existe pero no fue comenzada lanza una
	 *  TareaNoComenzadaException (no se puede finalizar una tarea que no se ha comenzado), sino indica como fecha final la fecha y hora actual.
	 */
	public void finalizar(Integer idTarea) throws TareaInexistenteException, TareaNoComenzadaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		Optional<Tarea> tarea = tareasAsignadas.stream().filter(t -> t.getId() == idTarea).findFirst();
		if(tarea.isEmpty()) {
			throw new TareaInexistenteException("La tarea que desea finalizar no se encuentra en la lista de tareas asignadas.");
		}
		else if(tarea.get().getFechaInicio() == null) {
			throw new TareaNoComenzadaException();
		}
		else {
			tarea.get().setFechaFin(LocalDateTime.now());
		}
	}

	//-------------------------
	//----- Ejercicio 2.e -----
	//-------------------------
	/** Busca la tarea en la lista de tareas asignadas: si no existe lanza una TareaInexistenteException, si existe se parsea la fecha
	 *  pasada como argumento para setearla como fecha inicial.
	 */
	public void comenzar(Integer idTarea,String fecha) throws TareaInexistenteException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora pasadas por parámetro
		Optional<Tarea> tarea = tareasAsignadas.stream().filter(t -> t.getId() == idTarea).findFirst();
		if(tarea.isEmpty()) {
			throw new TareaInexistenteException("La tarea que desea comenzar no se encuentra en la lista de tareas asignadas.");
		}
		else {
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			tarea.get().setFechaInicio(LocalDateTime.parse(fecha, formato));
		}
	}
	
	//-------------------------
	//----- Ejercicio 2.f -----
	//-------------------------
	/** Busca la tarea en la lista de tareas asignadas: si no existe lanza una TareaInexistenteException, si existe pero no fue comenzada lanza una
	 *  TareaNoComenzadaException (no se puede finalizar una tarea que no se ha comenzado), si la fecha de finalización es menor que la de inicio lanza
	 *  una TareaFinalizadaAntesDeComenzarException, sino se parsea la fecha pasada como argumento para setearla como fecha de finalización.
	 */
	public void finalizar(Integer idTarea,String fecha) throws TareaInexistenteException, TareaNoComenzadaException, TareaFinalizadaAntesDeComenzarException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora pasadas por parámetro
		Optional<Tarea> tarea = tareasAsignadas.stream().filter(t -> t.getId() == idTarea).findFirst();
		if(tarea.isEmpty()) {
			throw new TareaInexistenteException("La tarea que desea finalizar no se encuentra en la lista de tareas asignadas.");
		}
		else if(tarea.get().getFechaInicio() == null) {
			throw new TareaNoComenzadaException();
		}
		else {
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			LocalDateTime fechaFin = LocalDateTime.parse(fecha, formato);
			if(fechaFin.isBefore(tarea.get().getFechaInicio())) {
				throw new TareaFinalizadaAntesDeComenzarException();
			}
			else {
				tarea.get().setFechaFin(fechaFin);
			}
		}
	}
	
	//-------------------------
	//----- Ejercicio 4.a -----
	//-------------------------
	/** Permite verificar que la lista de empleados contenga a un empleado en particular.
	 */
	@Override
	public boolean equals(Object o) {
		Empleado e = (Empleado) o;
		return(this.cuil.equals(e.getCuil()) &&
			   this.nombre.equals(e.getNombre()) &&
			   this.tipo.equals(e.getTipo()) &&
			   this.costoHora.equals(e.getCostoHora()));
	}

	//-------------------------
	//----- Ejercicio 4.i -----
	//-------------------------
	public String asCsv() {
		return cuil+";"+nombre;
	}
	
	@Override
	public String toString() {
		return cuil +", "+nombre+", "+tipo+", "+costoHora;
	}
	
}
