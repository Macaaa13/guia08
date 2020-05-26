package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;
import frsf.isi.died.guia08.problema01.excepciones.EmpleadoInexistenteException;
import frsf.isi.died.guia08.problema01.excepciones.TareaInexistenteException;
import frsf.isi.died.guia08.problema01.excepciones.TareaNoComenzadaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHHTest {

	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	Empleado e3 = new Empleado(300, "Anna", Tipo.CONTRATADO, 300.0);
	Empleado e4 = new Empleado(400, "Matt", Tipo.EFECTIVO, 250.0);
	Tarea t1 = new Tarea(1, "Descripción 1", 3);
	AppRRHH app = new AppRRHH();
	
	//----- Ejercicio 4.a -----
	// Test del método agregarEmpleadoContratado
	@Test
	public void testAgregarEmpleadoContratado() {
		app.agregarEmpleadoContratado(100, "Juan", 150.0);
		assertTrue(app.getEmpleados().contains(e1));
	}
	
	//----- Ejercicio 4.b -----
	// Test del método asignarTarea
	@Test
	public void testAgregarEmpleadoEfectivo() {
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		assertTrue(app.getEmpleados().contains(e2));
	}
	
	//----- Ejercicio 4.c -----
	// Test del método agregarEmpleadoEfectivo
		@Test
		/** El empleado está en la lista de empleados y cumple las condiciones necesarias para ser
		 *  asignado a la tarea, por lo que se busca en esa lista al empleado y se verifica que tenga
		 *  la tarea asignada.
		 */
		public void testAsignarTarea() throws AsignacionIncorrectaException, EmpleadoInexistenteException {
			app.agregarEmpleadoContratado(100, "Juan", 150.0);
			app.asignarTarea(100, 1, "Descripción 1", 3);
			assertEquals(1, app.getEmpleados().stream()
											  .filter(e -> e.equals(e1) &&
					                                  e.getTareasAsignadas().stream().filter(t -> t.equals(t1)).count()==1)
										      .count());
		}
		
		
		@Test(expected = EmpleadoInexistenteException.class)
		/** El empleado no está en la lista de tareas, por lo que no puede ser asignado a una tarea.
		 */
		public void testAsignarTareaAEmpleadoInexistente() throws AsignacionIncorrectaException, EmpleadoInexistenteException {
			app.asignarTarea(100, 1, "Descripción 1", 3);
		}
		
		@Test(expected = AsignacionIncorrectaException.class)
		/** El empleado está en la lista de tareas, pero ya tiene más de 5 tareas asignadas, por lo que no es
		 *  posible realizar la asignación.
		 */
		public void testAsignarTareaEmpleadoContratadoSinCondicionesSuficientes() throws AsignacionIncorrectaException, EmpleadoInexistenteException {
			app.agregarEmpleadoContratado(100, "Juan", 150.0);
			app.asignarTarea(100, 1, "Descripción 1", 3);
			app.asignarTarea(100, 2, "Descripción 2", 12);
			app.asignarTarea(100, 3, "Descripción 3", 4);
			app.asignarTarea(100, 4, "Descripción 4", 6);
			app.asignarTarea(100, 5, "Descripción 5", 10);
			app.asignarTarea(100, 6, "Descripción 6", 4);
			app.asignarTarea(100, 7, "Descripción 7", 7); //No debería poder asignarse
		}
		
		@Test(expected = AsignacionIncorrectaException.class)
		public void testAsignarTareaEmpleadoEfectivoSinCondicionesSuficientes() throws AsignacionIncorrectaException, EmpleadoInexistenteException {
			app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
			//Al no tener tareas la cantidad de horas estimadas de trabajo es 0 y puede asignarse la tarea
			app.asignarTarea(200, 1, "Descripción 1", 12); 
			//Al tener 12 horas estimadas de trabajo, puede asignarse la tarea
			app.asignarTarea(200, 2, "Descripción 2", 4);
			//Como tiene 16 horas estimadas de trabajo, es decir más de 15, no puede asignarse la tarea
			app.asignarTarea(200, 3, "Descripción 3", 5);
		}
		
		//----- Ejercicio 4.d -----
		// Test del método empezarTarea
		@Test
		/** El empleado está en la lista de empleados, fue asignado a la tarea y puede comenzarla, por lo que la
		 *  fecha de inicio de la tarea debe ser distinta de null
		 */
		public void testEmpezarTarea() throws EmpleadoInexistenteException, TareaInexistenteException, AsignacionIncorrectaException {
			app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
			app.asignarTarea(200, 1, "Descripción 1", 3);
			app.empezarTarea(200, 1);
			assertEquals(1, app.getEmpleados().stream()
					  .filter(e -> e.equals(e2) &&
                            e.getTareasAsignadas().stream().filter(t -> t.equals(t1) &&
                            											t.getFechaInicio()!=null).count()==1)
				      .count());	
		}
		
		@Test(expected = EmpleadoInexistenteException.class)
	    /** El empleado no está en la lista de empleados por lo que no se le puede indicar que comience la tarea
	     */
		public void testEmpezarTareaEmpleadoInexistente() throws TareaInexistenteException, EmpleadoInexistenteException {
			app.empezarTarea(200, 1);
		}
		
		@Test(expected = TareaInexistenteException.class)
		/** El empleado está en la lista de empleados pero no fue asignado a la tarea, por lo que no se le puede
		 *  indicar que la comience
	     */
		public void testEmpezarTareaInexistente() throws TareaInexistenteException, EmpleadoInexistenteException {
			app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
			app.empezarTarea(200, 1);
		}
		
		//----- Ejercicio 4.e -----
		// Test del método terminarTarea
		@Test
		/** El empleado está en la lista de empleados, fue asignado a la tarea, la comenzó y puede terminarla, 
		 *  por lo que la fecha final de la tarea debe ser distinta de null
		 */
		public void testTerminarTarea() throws AsignacionIncorrectaException, EmpleadoInexistenteException, TareaInexistenteException, TareaNoComenzadaException {
			app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
			app.asignarTarea(200, 1, "Descripción 1", 3);
			app.empezarTarea(200, 1);
			app.terminarTarea(200, 1);
			assertEquals(1, app.getEmpleados().stream()
					  .filter(e -> e.equals(e2) &&
                          e.getTareasAsignadas().stream().filter(t -> t.equals(t1) &&
                          											t.getFechaFin()!=null).count()==1)
				      .count());	
		}
		
		@Test(expected = EmpleadoInexistenteException.class)
		/** El empleado no está en la lista de empleados por lo que no se le puede indicar que termine la tarea
	     */
		public void testTerminarTareaEmpleadoInexistente() throws TareaInexistenteException, TareaNoComenzadaException, EmpleadoInexistenteException {
			app.terminarTarea(200, 1);
		}
		
		@Test(expected = TareaInexistenteException.class)
		public void testTerminarTareaInexistente() throws TareaInexistenteException, TareaNoComenzadaException, EmpleadoInexistenteException {
			app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
			app.terminarTarea(200, 1);
		}
		
		@Test(expected = TareaNoComenzadaException.class)
		public void testTerminarTareaNoComenzada() throws TareaInexistenteException, TareaNoComenzadaException, EmpleadoInexistenteException, AsignacionIncorrectaException {
			app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
			app.asignarTarea(200, 1, "Descripción 1", 3);
			app.terminarTarea(200, 1);
		}
		
		//----- Ejercicio 4.f -----
		// Test del método cargarEmpleadosContratadosCSV
		@Test
		/** tareas.csv
		 *  100;"Juan";150.0
		 *  300;"Anna";300.0
		 */
		public void testCargarEmpleadosContratadosCSV() throws FileNotFoundException, IOException {
			try(Writer fileWriter = new FileWriter("tareas.csv")){
				try(BufferedWriter out = new BufferedWriter(fileWriter)){
					out.write(e1.getCuil()+";\""+e1.getNombre()+"\";"+e1.getCostoHora()+System.getProperty("line.separator"));
					out.write(e3.getCuil()+";\""+e3.getNombre()+"\";"+e3.getCostoHora()+System.getProperty("line.separator"));
				}
			}
			app.cargarEmpleadosContratadosCSV("tareas.csv");
			long l = app.getEmpleados().stream().filter(e -> e.getCuil().equals(e1.getCuil()) ||
													         e.getCuil().equals(e3.getCuil()))
									   .count();
			assertEquals(2,l);
		}
		
		@Test(expected = FileNotFoundException.class)
		public void testCargarEmpleadosContratadosCSVArchivoInexistente() throws FileNotFoundException, IOException {
			app.cargarEmpleadosContratadosCSV("tareasInexistente.csv");
		}
		
		//----- Ejercicio 4.g -----
		// Test del método cargarEmpleadosEfectivosCSV
		@Test
		/** tareas.csv
		 *  200;"Marta";200.0
		 *  400;"Matt";250.0
		 */
		public void testCargarEmpleadosEfectivosCSV() throws FileNotFoundException, IOException {
			try(Writer fileWriter = new FileWriter("tareas.csv")){
				try(BufferedWriter out = new BufferedWriter(fileWriter)){
					out.write(e2.getCuil()+";\""+e2.getNombre()+"\";"+e2.getCostoHora()+System.getProperty("line.separator"));
					out.write(e4.getCuil()+";\""+e4.getNombre()+"\";"+e4.getCostoHora()+System.getProperty("line.separator"));
				}
			}
			app.cargarEmpleadosEfectivosCSV("tareas.csv");
			long l = app.getEmpleados().stream().filter(e -> e.getCuil().equals(e2.getCuil()) ||
													         e.getCuil().equals(e4.getCuil()))
									   .count();
			assertEquals(2,l);
		}
		
		@Test(expected = FileNotFoundException.class)
		public void testCargarEmpleadosEfectivosCSVArchivoInexistente() throws FileNotFoundException, IOException {
			app.cargarEmpleadosEfectivosCSV("tareasInexistente.csv");
		}
}
