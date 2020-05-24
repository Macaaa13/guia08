package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;
import frsf.isi.died.guia08.problema01.excepciones.TareaFinalizadaAntesDeComenzarException;
import frsf.isi.died.guia08.problema01.excepciones.TareaInexistenteException;
import frsf.isi.died.guia08.problema01.excepciones.TareaNoComenzadaException;

public class Empleado {

	//--- Atributos ---
	public enum Tipo { CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	
	private Function<Tarea, Double> calculoPagoPorTarea;
	private Predicate<Empleado> puedeAsignarTarea;

	
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
	
	public void configurarEfectivo() {
		puedeAsignarTarea = e -> e.efectivoTareasPendientes()<=15;
		calculoPagoPorTarea = t -> {
			if(t.tareaAdelantada()) { return t.getDuracionEstimada()*this.costoHora*1.2;}
			return t.getDuracionEstimada()*this.costoHora;
		};
	}
	
	public void configurarContratado() {
		puedeAsignarTarea = e -> e.tareasAsignadas.size()<=5;
		calculoPagoPorTarea = t -> {
			if(t.tareaAdelantada()) { return t.getDuracionEstimada()*this.costoHora*1.3;}
			if(t.tareaAtrasada()) { return t.getDuracionEstimada()*this.costoHora*0.75; }
			return t.getDuracionEstimada()*this.costoHora;
		};	
	}
	
	//-------------------------
	//----- Ejercicio 2.a -----
	//-------------------------
	/** - Empleados contratados -> No pueden tener más de 5 tareas asignadas pendientes
	 *  - Empleados efectivos -> No pueden tener tareas asignadas pendientes que sumen
	 *  más de 15 horas de trabajo estimado
	 *  - En caso de asignar un empleado a una tarea que ya tiene otro empleado asignado
	 *  o a una tarea que ya fue finalizada, se lanza una excepción
	 */
	public Boolean asignarTarea(Tarea t) throws AsignacionIncorrectaException {
		if(t.getEmpleadoAsignado() != null) {
			throw new AsignacionIncorrectaException("Debe seleccionar una tarea que no tenga un empleado asignado.");
		}
		if(t.getFechaFin() != null) {
			throw new AsignacionIncorrectaException("Debe seleccionar una tarea que no haya sido finalizada.");
		}
		return puedeAsignarTarea.test(this);
	}

	/** Comprueba que las tareas pendientes del empleado no sumen más de 15 horas
	 */
	public Integer efectivoTareasPendientes() {
		return tareasAsignadas.stream().mapToInt(t -> t.getDuracionEstimada()).sum();
	}
	
	//-------------------------
	//----- Ejercicio 2.b -----
	//-------------------------
	public Double salario() {
		// cargar todas las tareas no facturadas
		// calcular el costo
		// marcarlas como facturadas.
		
		return tareasAsignadas.stream()
				   .filter(t -> t.getFacturada() == false)
				   .map(t -> {
					   t.setFacturada(true);
					   return t;
				   })
				   .mapToDouble(t -> this.costoTarea(t))
				   .sum();
		
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		return this.calculoPagoPorTarea.apply(t);
	}
	
	//-------------------------
	//----- Ejercicio 2.c -----
	//-------------------------
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
	/** Considero necesaria la creación de una excepción que se lance si se trata de finalizar
	 *  una tarea que no ha sido comenzada
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
	public void comenzar(Integer idTarea,String fecha) throws TareaInexistenteException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
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
	/** Considero necesaria la creación de una excepción que se lance si se trata de finalizar
	 *  una tarea en una fecha anterior a la fecha de comienzo
	 */
	public void finalizar(Integer idTarea,String fecha) throws TareaInexistenteException, TareaNoComenzadaException, TareaFinalizadaAntesDeComenzarException {
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
}
