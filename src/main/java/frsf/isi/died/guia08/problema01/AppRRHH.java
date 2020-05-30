package frsf.isi.died.guia08.problema01;

import java.io.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.excepciones.*;
import frsf.isi.died.guia08.problema01.modelo.*;
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
	/** Crea un emplado contratado y lo agrega a la lista de empleados.
	 */
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
	/** Crea un emplado efectivo y lo agrega a la lista de empleados.
	 */
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
	/** Busca el empleado en la lista de empleados según su cuil. Si no existe, lanza una EmpleadoInexistenteException.
	 *  Si existe, se crea una tarea con los parámetros pasados y se la asigna al empleado, lanzando una AsignacionIncorrectaException
	 *  si ésta ya tiene otro empleado asignado o si el empleado no cumple las condiciones necesarias.
	 *  
	 *  Si bien deberia contemprarse el caso de asignar una tarea ya finalizada, esto no puede ocurrir debido a que tanto tareas como
	 *  empleados en esta clase son creados desde 0, y en el caso de las tareas ninguna es creada con la fecha de finalización inicializada.
	 *  
	 *  Para el caso de que una tarea ya tenga un empleado asignado, este método verifica que los empleados de la lista de empleados no
	 *  contengan la tarea en su lista de tareas asignadas, ya que eso implicaría que llamaron a este método y por lo tanto ya estarían
	 *  asignados.
	 */
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) throws AsignacionIncorrectaException, EmpleadoInexistenteException {
		// crear un empleado
		// con el método buscarEmpleado() de esta clase
		// agregarlo a la lista	
		Optional<Empleado> opt = this.buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(opt.isPresent()) {
			Tarea t = new Tarea(idTarea, descripcion, duracionEstimada);
			int tareaYaAsignada = 0;
			for(Empleado e: empleados) {
					if(e.getTareasAsignadas().contains(t)) {
						tareaYaAsignada++;
					}
			}
			if(tareaYaAsignada==0) {
				t.asignarEmpleado(opt.get());
			}
			else {
				throw new AsignacionIncorrectaException("Debe seleccionar una tarea que no tenga un empleado asignado.");
			}
		}
		else {
			throw new EmpleadoInexistenteException("El empleado al que desea asignarle la tarea no existe.");
		}
	}
	
	//-------------------------
	//----- Ejercicio 4.d -----
	//-------------------------
	/** Busca el empleado en la lista de empleados según su cuil. Si no existe, lanza una EmpleadoInexistenteException.
	 *  Si existe, se le indica que comience la tarea. La función comenzar busca la tarea en la lista de tareas asignadas: si no existe lanza
	 *  una TareaInexistenteException, si existe indica como fecha inicial la fecha y hora actual.
	 */
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
	/** Busca el empleado en la lista de empleados según su cuil. Si no existe, lanza una EmpleadoInexistenteException.
	 *  Si existe, se le indica que finalice la tarea. La función finalizar busca la tarea en la lista de tareas asignadas: si no existe lanza
	 *  una TareaInexistenteException, si existe pero no fue comenzada lanza una TareaNoComenzadaException (no se puede finalizar una tarea que
	 *  no se ha comenzado), sino indica como fecha final la fecha y hora actual.
	 */
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
	/** Lee los datos de un archivo CSV que contiene Empleados, y para cada uno invoca a la función agregarEmpleadoContratado.
	 */
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

	//-------------------------
	//----- Ejercicio 4.g -----
	//-------------------------
	/** Lee los datos de un archivo CSV que contiene Empleados, y para cada uno invoca a la función agregarEmpleadoEfectivo.
	 */
	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoEfectivo
		try (Reader fileReader = new FileReader(nombreArchivo)) {
			try (BufferedReader in = new BufferedReader(fileReader)) {
				String linea = null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
						this.agregarEmpleadoEfectivo(Integer.valueOf(fila[0]), fila[1], Double.valueOf(fila[2]));
				}
			}
		}
	}

	//-------------------------
	//----- Ejercicio 4.h -----
	//-------------------------
	/** Lee los datos de un archivo CSV que contiene Tareas y el cuil del empleado asignado, cargando las tareas en la lista de tareas asignadas
	 *  del empleado correspondiente.
	 *  [ En el enunciado indica que el orden es id, descripción, duración estimada y cuil del empleado asignado, pero en eclipse indica que el 
	 *  cuil del empleado asignado va primero y es en éste último en el que se basa este método ]
	 */
	public void cargarTareasCSV(String nombreArchivo) throws FileNotFoundException, IOException, AsignacionIncorrectaException, EmpleadoInexistenteException {
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la tarea, descripcion y duración estimada en horas.
		try (Reader fileReader = new FileReader(nombreArchivo)) {
			try (BufferedReader in = new BufferedReader(fileReader)) {
				String linea = null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
					asignarTarea(Integer.valueOf(fila[0]),Integer.valueOf(fila[1]),fila[2], Integer.valueOf(fila[3]));
				}
			}
		}
	}
	
	//-------------------------
	//----- Ejercicio 4.i -----
	//-------------------------
	/** Guarda todas las tareas que fueron terminadas y no facturadas en un archivo CSV indicando información sobre la tarea, junto con
	 *  cuil y nombre del empleado que la finalizó.
	 */
	private void guardarTareasTerminadasCSV() throws IOException {
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
		List<Tarea> lista = empleados.stream().map(e -> e.getTareasAsignadas()).flatMap(List::stream).collect(Collectors.toList());
		try(Writer fileWriter = new FileWriter("tareasTerminadas.csv")){
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				for(Tarea t: lista) {
					if(!t.getFacturada() && t.getFechaFin()!=null) {
						out.write(t.asCsv()+";"+t.getEmpleadoAsignado().asCsv());
						out.newLine();
					}
				}
			}
		}
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() throws IOException {
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
