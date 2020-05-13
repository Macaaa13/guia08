package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class EmpleadoTest {

	Tarea t1 = new Tarea(1,"descripción 1", 3);
	Tarea t2 = new Tarea(2,"descripción 2", 4);
	Tarea t3 = new Tarea(3,"descripción 3", 2);
	Tarea t4 = new Tarea(4,"descripción 4", 7);
	Tarea t5 = new Tarea(5,"descripción 5", 5);
	Tarea t6 = new Tarea(6,"descripción 6", 3);
	Tarea t7 = new Tarea(7,"descripción 7", 6);
	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	
	@Before
	public void init() throws AsignacionIncorrectaException {
		e1.getTareasAsignadas().add(t1);
		e1.getTareasAsignadas().add(t2);
		e1.getTareasAsignadas().add(t3);
		e1.getTareasAsignadas().add(t4);
		e1.getTareasAsignadas().add(t5);
		e2.getTareasAsignadas().add(t4); // 7 horas de trabajo estimado
		e2.getTareasAsignadas().add(t5); // 12 horas de trabajo estimado
		e2.getTareasAsignadas().add(t6); // 15 horas de trabajo estimado
	}
	
	//Ejercicio 2.a
	@Test
	/**
	 *  Como el empleado es contratado y no tiene más de 5 tareas pendientes asignadas, se le puede asignar la tarea
	 */
	public void testAsignarTareaContratadoMenosDe5TareasPendientes() throws AsignacionIncorrectaException {
		assertTrue(e1.asignarTarea(t6));
	}
	
	@Test
	/**
	 *  Como el empleado es contratado y tiene más de 5 tareas pendientes asignadas, no se le puede asignar la tarea
	 */
	public void testAsignarTareaContratadoMasde5TareasPendientes() throws AsignacionIncorrectaException {
		e1.getTareasAsignadas().add(t6);
		assertFalse(e1.asignarTarea(t7));
	}
	
	@Test
	/**
	 *  Como el empleado es efectivo y las tareas pendientes asignadas no suman más de 15 horas
	 *  de trabajo estimado, se le puede asignar la tarea
	 */
	public void testAsignarTareaEfectivo15HorasOMenos() throws AsignacionIncorrectaException {
		assertTrue(e2.asignarTarea(t1));
	}
	
	@Test
	/**
	 *  Como el empleado es efectivo y las tareas pendientes asignadas suman más de 15 horas
	 *  de trabajo estimado, no se le puede asignar la tarea
	 */
	public void testAsignarTareaEfectivoMasDe15Horas() throws AsignacionIncorrectaException {
		e2.getTareasAsignadas().add(t2); // 19 horas
		assertFalse(e2.asignarTarea(t1));
	}
	
	@Test
	public void testSalario() {
		fail("Not yet implemented");
	}

	@Test
	public void testCostoTarea() {
		fail("Not yet implemented");
	}

	@Test
	public void testComenzarInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testFinalizarInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testComenzarIntegerString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFinalizarIntegerString() {
		fail("Not yet implemented");
	}

}
