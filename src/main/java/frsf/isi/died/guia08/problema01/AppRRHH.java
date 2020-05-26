package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;
import frsf.isi.died.guia08.problema01.excepciones.EmpleadoInexistenteException;
import frsf.isi.died.guia08.problema01.excepciones.TareaInexistenteException;
import frsf.isi.died.guia08.problema01.excepciones.TareaNoComenzadaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class AppRRHH {

	//--- Atributos ---
	private List<Empleado> empleados;

	//--- Constructor ---
	public AppRRHH() {
	empleados = new ArrayList<Empleado>();
	}
	
	//--- Getters y Setters ---
	public List<Empleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}

	//--- Métodos ---
	//-------------------------
	//----- Ejercicio 4.a -----
	//-------------------------
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista
		Empleado e = new Empleado(cuil, nombre, Tipo.CONTRATADO, costoHora);
		e.configurarContratado();
		this.empleados.add(e);
	}
	
	//-------------------------
	//----- Ejercicio 4.b -----
	//-------------------------
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista
		Empleado e = new Empleado(cuil, nombre, Tipo.EFECTIVO, costoHora);
		e.configurarEfectivo();
		this.empleados.add(e);
	}
	
	//-------------------------
	//----- Ejercicio 4.c -----
	//-------------------------
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) throws AsignacionIncorrectaException, EmpleadoInexistenteException {
		// crear un empleado
		// con el método buscarEmpleado() de esta clase
		// agregarlo a la lista	
		Optional<Empleado> opt = this.buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(opt.isPresent()) {
			Tarea t = new Tarea(idTarea, descripcion, duracionEstimada);
			t.asignarEmpleado(opt.get());
		}
		else {
			throw new EmpleadoInexistenteException("El empleado al que desea asignarle la tarea no existe.");
		}
	}
	
	//-------------------------
	//----- Ejercicio 4.d -----
	//-------------------------
	public void empezarTarea(Integer cuil,Integer idTarea) throws TareaInexistenteException, EmpleadoInexistenteException {
		// busca el empleado por cuil en la lista de empleados
		// con el método buscarEmpleado() actual de esta clase
		// e invoca al método comenzar tarea
		Optional<Empleado> opt = this.buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(opt.isPresent()) {
			opt.get().comenzar(idTarea);
		}
		else {
			throw new EmpleadoInexistenteException("El empleado que busca no existe.");
		}
	}
	
	//-------------------------
	//----- Ejercicio 4.e -----
	//-------------------------
	public void terminarTarea(Integer cuil,Integer idTarea) throws TareaInexistenteException, TareaNoComenzadaException, EmpleadoInexistenteException {
		// busca el empleado por cuil en la lista de empleados
		// con el método buscarEmpleado() actual de esta clase
		// e invoca al método finalizar tarea	
		Optional<Empleado> opt = this.buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(opt.isPresent()) {
			opt.get().finalizar(idTarea);
		}
		else {
			throw new EmpleadoInexistenteException("El empleado que busca no existe.");
		}
	}

	//-------------------------
	//----- Ejercicio 4.f -----
	//-------------------------
	public void cargarEmpleadosContratadosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
		try (Reader fileReader = new FileReader(nombreArchivo)) {
			try (BufferedReader in = new BufferedReader(fileReader)) {
				String linea = null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
						this.agregarEmpleadoContratado(Integer.valueOf(fila[0]), fila[1], Double.valueOf(fila[2]));
				}
			}
		}
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado		
	}

	public void cargarTareasCSV(String nombreArchivo) {
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
	}
	
	private void guardarTareasTerminadasCSV() {
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}

	@Override
	public String toString() {
		return empleados.toString();
	}
	
}
