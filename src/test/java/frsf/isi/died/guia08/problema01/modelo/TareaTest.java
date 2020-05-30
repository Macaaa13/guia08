package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;
import org.junit.*;

import java.time.LocalDateTime;

import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {

	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	Tarea t1 = new Tarea(1,"Descripción 1", 12); 
	Tarea t2 = new Tarea(2,"Descripción 2", 2); 
	Tarea t3 = new Tarea(3,"Descripción 3", 4); 
	Tarea t4 = new Tarea(4,"Descripción 4", 7); 
	Tarea t5 = new Tarea(5,"Descripción 5", 5); 
	Tarea t6 = new Tarea(6,"Descripción 6", 3);
	
	@Before
	public void init() {
		//Empleado e1
		e1.configurarContratado();
		e1.getTareasAsignadas().add(t1);
		e1.getTareasAsignadas().add(t2);
		e1.getTareasAsignadas().add(t3);
		e1.getTareasAsignadas().add(t4);
		//Empleado e2
		e2.configurarEfectivo();
		e2.getTareasAsignadas().add(t1);
	}
	
	//-------------------------
	//----- Ejercicio 2.b -----
	//-------------------------
	
	// Test del método tareaAdelantada
	/** Si la tarea está adelantada, la diferencia entre la duración estimada y la real debe ser positiva.
	 *  La tarea t1 es seteada para terminarse en 2 días, por lo que su duración real es 8 horas.
	 *  Como su duración estimada es 12 horas, es decir 3 días, la tarea fue finalizada antes de lo estimado.
	 */
	@Test
	public void testTareaAdelantada() {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 2, 14, 0));
		assertTrue(t1.tareaAdelantada());
	}
	
	/** La tarea t1 es seteada para terminarse en 4 días, por lo que su duración real es 16 horas.
	 *  Como su duración estimada es 12 horas, es decir 3 días, la tarea no fue finalizada antes de lo estimado.
	 */
	@Test
	public void testTareaNoAdelantada() {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 4, 14, 0));
		assertFalse(t1.tareaAdelantada());
	}
	
	// Test del método tareaAtrasada
	/** Una tarea está atrasada si tardó más de 2 días de lo estimado en finalizarse, es decir, si tardó más de 8 horas.
	 *  En dicho caso, la diferencia entre la duración estimada y la real debe ser menor que -8.
	 *  La tarea t1 es seteada para terminarse en 6 días, por lo que su duración real es 24 horas.
	 *  Como su duración estimada es de 12 horas, es decir 3 días, la tarea fue finalizada 3 días tarde y por lo tanto se considera atrasada.
	 */
	@Test
	public void testTareaAtrasada() {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 6, 14, 0));
		assertTrue(t1.tareaAtrasada());
	}
	
	/** La tarea t1 es seteada para terminarse en 5 días, por lo que su duración real es 20 horas.
	 *  Como su duración estimada es de 12 horas, es decir 3 días, la tarea fue finalizada 2 días tarde y por lo tanto no se considera atrasada
	 *  (deben ser más de 2 días tarde para considerarla atrasada).
	 */
	@Test
	public void testTareaNoAtrasada() {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 5, 14, 0));
		assertFalse(t1.tareaAtrasada());
	}
	
	//-------------------------
	//------ Ejercicio 3 ------
	//-------------------------
	
	// Test del método asignarEmpleado
	/** Se asigna el empleado e1 a la tarea t6, por lo que si trata de asignarse luego el empleado e2 a la tarea t6 se lanza una
	 *  AsignacionIncorrectaException.
	 */
	@Test(expected = AsignacionIncorrectaException.class)
	public void testAsignarEmpleadoATareaConEmpleadoYaAsignado() throws AsignacionIncorrectaException {
		t6.asignarEmpleado(e1);
		t6.asignarEmpleado(e2);
	}
	
	/** Se le setea a la tarea t6 una fecha de finalización, entonces cuando se quiere asignar la tarea al empleado e2
	 *  lanza una AsignacionIncorrectaException (no se puede asignar una tarea que ya ha sido finalizada).
	 * @throws AsignacionIncorrectaException 
	 */
	@Test(expected = AsignacionIncorrectaException.class)
	public void testAsignarEmpleadoATareaYaFinalizada() throws AsignacionIncorrectaException {
		t6.setFechaFin(LocalDateTime.now());
		t6.asignarEmpleado(e2);
	}
	
	/** Los empleados contratados no pueden tener más de 5 tareas asignadas pendientes.
	 *  Se agrega la tarea t5 a la lista de tareas asignadas del empleado contratado e1, de forma que tenga 5 tareas asignadas pendientes
	 *  y al intentar asignarlo a la tarea t6 se lance una AsignacionIncorrectaException.  
	 */
	@Test(expected = AsignacionIncorrectaException.class)
	public void testAsignarEmpleadoContratadoSinCondicionesNecesarias() throws AsignacionIncorrectaException {
		e1.getTareasAsignadas().add(t5);
		t6.asignarEmpleado(e1);
	}
	
	/** Los empleados efectivos no pueden tener tareas asignadas pendientes que sumen más de 15 horas de trabajo estimado.
	 *  Se agrega la tarea t2 a la lista de tareas asignadas del empleado efectivo e2, sumando un total de 14 horas de trabajo estimado.
	 *  Al intentar asignarle a la tarea t3 el empleado e2, el total de horas de trabajo estimado pasa a ser 18, que al ser mayor que 15
	 *  no permite que el empleado pueda ser asignado.
	 */
	@Test(expected = AsignacionIncorrectaException.class)
	public void testAsignarEmpleadoEfectivoSinCondicionesNecesarias() throws AsignacionIncorrectaException {
		e2.getTareasAsignadas().add(t2);
		t3.asignarEmpleado(e2);
	}
	
	/** Los empleados contratados no pueden tener más de 5 tareas asignadas pendientes.
	 *  Como el empleado e1 tiene 4 tareas asignadas pendientes, puede ser asignado a la tarea t5.
	 */
	@Test
	public void testAsignarEmpleadoContratado() throws AsignacionIncorrectaException {
		t5.asignarEmpleado(e1);
		assertEquals(e1, t5.getEmpleadoAsignado());
		
	}
	
	/** Los empleados efectivos no pueden tener tareas asignadas pendientes que sumen más de 15 horas de trabajo estimado.
	 *  Como el empleado e2 tiene 12 horas de trabajo estimado, si se asigna a la tarea t2 tendrá 14 horas de trabajo estimado, que al ser
	 *  menor que 15 permite que al empleado pueda ser asignado.
	 */
	@Test
	public void testAsignarEmpleadoEfectivo() throws AsignacionIncorrectaException {
		t2.asignarEmpleado(e2);
		assertEquals(e2, t2.getEmpleadoAsignado());
		
	}

}
