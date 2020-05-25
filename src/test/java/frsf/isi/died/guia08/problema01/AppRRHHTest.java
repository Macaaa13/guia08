package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHHTest {

	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	Tarea t1 = new Tarea(1, "Descripción 1", 3);
	AppRRHH app = new AppRRHH();
	
	@Before
	public void init() throws AsignacionIncorrectaException {
		e1.configurarContratado();
		e2.configurarEfectivo();
	}
	
	//----- Ejercicio 4.a -----
	@Test
	public void testAgregarEmpleadoContratado() {
		app.agregarEmpleadoContratado(100, "Juan", 150.0);
		assertTrue(app.getEmpleados().contains(e1));
	}
	
	//----- Ejercicio 4.b -----
	@Test
	public void testAgregarEmpleadoEfectivo() {
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		assertTrue(app.getEmpleados().contains(e2));
	}
	
	//----- Ejercicio 4.c -----
	// Test del método asignarTarea
		@Test
		/** El empleado está en la lista de empleados y cumple las condiciones necesarias para ser
		 *  asignado a la tarea, por lo que se busca en esa lista al empleado y se verifica que tenga
		 *  la tarea asignada.
		 */
		public void testAsignarTarea() throws AsignacionIncorrectaException {
			app.agregarEmpleadoContratado(100, "Juan", 150.0);
			app.asignarTarea(100, 1, "Descripción 1", 3);
			assertEquals(1, app.getEmpleados().stream()
											  .filter(e -> e.equals(e1) &&
					                                  e.getTareasAsignadas().stream().filter(t -> t.equals(t1)).count()==1)
										      .count());							   
		}
		
		
		@Test(expected = AsignacionIncorrectaException.class)
		/** El empleado no está en la lista de tareas, por lo que no puede ser asignado a una tarea.
		 */
		public void testAsignarTareaAEmpleadoInexistente() throws AsignacionIncorrectaException {
			app.asignarTarea(100, 1, "Descripción 1", 3);
		}
		
		@Test(expected = AsignacionIncorrectaException.class)
		/** El empleado está en la lista de tareas, pero ya tiene más de 5 tareas asignadas, por lo que no es
		 *  posible realizar la asignación.
		 */
		public void testAsignarTareaEmpleadoContratadoSinCondicionesSuficientes() throws AsignacionIncorrectaException {
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
		public void testAsignarTareaEmpleadoEfectivoSinCondicionesSuficientes() throws AsignacionIncorrectaException {
			app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
			//Al no tener tareas la cantidad de horas estimadas de trabajo es 0 y puede asignarse la tarea
			app.asignarTarea(200, 1, "Descripción 1", 12); 
			//Al tener 12 horas estimadas de trabajo, puede asignarse la tarea
			app.asignarTarea(200, 2, "Descripción 2", 4);
			//Como tiene 16 horas estimadas de trabajo, es decir más de 15, no puede asignarse la tarea
			app.asignarTarea(200, 3, "Descripción 3", 5);
		}
		
}
