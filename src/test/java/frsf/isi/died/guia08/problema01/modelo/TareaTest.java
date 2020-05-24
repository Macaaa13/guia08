package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {

	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	Tarea t1 = new Tarea(1,"descripción 1", 12);
	Tarea t2 = new Tarea(2,"descripción 2", 3);
	Tarea t3 = new Tarea(3,"descripción 3", 4);
	Tarea t4 = new Tarea(4,"descripción 4", 6);
	Tarea t5 = new Tarea(5,"descripción 5", 10);
	Tarea t6 = new Tarea(6,"descripción 6", 16);
	Tarea t7 = new Tarea(7,"descripción 7", 4);
	
	@Before
	public void init() throws AsignacionIncorrectaException {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		e1.configurarContratado();
		e2.configurarEfectivo();
		e1.getTareasAsignadas().add(t1);
		e1.getTareasAsignadas().add(t2);
		e1.getTareasAsignadas().add(t3);
		e1.getTareasAsignadas().add(t4);
		e1.getTareasAsignadas().add(t5);
		e2.getTareasAsignadas().add(t1);
		e2.getTareasAsignadas().add(t2);
	}
	
	//----- Ejercicio 2.b -----
	// Test del método tareaAdelantada
	@Test
	public void tareaAdelantadaTest() {
		//Comienza el 1, termina el 3 -> Horas de trabajo total: 8
		t1.setFechaFin(LocalDateTime.of(2020, 9, 3, 22, 0));
		//Horas estimadas: 12 -> Terminó antes
		assertTrue(t1.tareaAdelantada());
	}
	
	@Test
	public void tareaNoAdelantadaTest() {
		//Comienza el 1, termina el 5 -> Horas de trabajo total: 16
		t1.setFechaFin(LocalDateTime.of(2020, 9, 5, 22, 0));
		//Horas estimadas: 12 -> Terminó después
		assertFalse(t1.tareaAdelantada());
	}
	
	@Test
	public void tareaTerminadaATiempoTest() {
		//Comienza el 1, termina el 4 -> Horas de trabajo total: 12
		t1.setFechaFin(LocalDateTime.of(2020, 9, 4, 22, 0));
		//Horas estimadas: 12 -> Terminó a tiempo
		assertFalse(t1.tareaAdelantada());
	}
	
	//Test del método tareaAtrasada()
	// Una tarea esta atrasada si el empleado demoró más de 2 días en finalizarla,
	// es decir, si tardó más de 8 horas en terminarla.
	@Test
	public void tareaAtrasadaTest() {
		//Comienza el 1, termina el 7 -> Horas de trabajo total: 24
		t1.setFechaFin(LocalDateTime.of(2020, 9, 7, 22, 0));
		//Horas estimadas: 12 -> Terminó 3 días tarde
		assertTrue(t1.tareaAtrasada());
	}
	
	@Test
	public void tareaNoAtrasadaTest() {
		//Comienza el 1, termina el 6 -> Horas de trabajo total: 20
		t1.setFechaFin(LocalDateTime.of(2020, 9, 6, 22, 0));
		//Horas estimadas: 12 -> Terminó 2 días tarde
		assertFalse(t1.tareaAtrasada());
	}
	
	//----- Ejercicio 3 -----
	// Test del metodo asignarEmpleado
	@Test
	/** El empleado e1 tiene 5 tareas en la lista de tareas asignadas, por lo que debería poder asignarse
	 *  en la tarea t6
	 */
	public void asignarEmpleadoContratadoTest() throws AsignacionIncorrectaException {
		t6.asignarEmpleado(e1);
		assertEquals(e1, t6.getEmpleadoAsignado());
	}
	
	@Test(expected = AsignacionIncorrectaException.class)
	/** El empleado e1 tiene 6 tareas en la lista de tareas asignadas, por lo que no debería poder asignarse
	 *  en la tarea t6
	 */
	public void asignarEmpleadoContratadoSinCondicionesNecesariasTest() throws AsignacionIncorrectaException {
		e1.getTareasAsignadas().add(t6);
		t7.asignarEmpleado(e1);
	}
	
	@Test
	/** El empleado e2 tiene 2 tareas en la lista de tareas asignadas que suman exactamente 15 horas de trabajo
	 *  estimado, por lo que debería poder asignarse al empleado en la tarea t3
	 */
	public void asignarEmpleadoEfectivoTest() throws AsignacionIncorrectaException {
		t3.asignarEmpleado(e2);
		assertEquals(e2, t3.getEmpleadoAsignado());
	}
	
	@Test(expected = AsignacionIncorrectaException.class)
	/** El empleado e2 tiene 3 tareas en la lista de tareas asignadas que suman más 15 horas de trabajo estimado, 
	 *  por lo que no debería poder asignarse al empleado en la tarea t4
	 */
	public void asignarEmpleadoEfectivoSinCondicionesNecesariasTest() throws AsignacionIncorrectaException {
		e2.getTareasAsignadas().add(t3);
		t4.asignarEmpleado(e2);
	}
	
	@Test(expected = AsignacionIncorrectaException.class)
	public void asignarEmpleadoConEmpleadoYaAsignadoTest() throws AsignacionIncorrectaException {
		t6.setEmpleadoAsignado(e1);
		t6.asignarEmpleado(e2);
	}
	
	@Test(expected = AsignacionIncorrectaException.class)
	public void asignarEmpleadoConTareaFinalizadaTest() throws AsignacionIncorrectaException {
		t3.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t3.setFechaFin(LocalDateTime.of(2020, 9, 3, 22, 0));
		t3.asignarEmpleado(e2);
	}

}
