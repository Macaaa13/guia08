package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;
import java.time.LocalDateTime;
import org.junit.*;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.excepciones.AsignacionIncorrectaException;
import frsf.isi.died.guia08.problema01.excepciones.TareaInexistenteException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class EmpleadoTest {

	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	Empleado e3 = new Empleado(300, "Matías", Tipo.EFECTIVO, 300.0);
	Empleado e4 = new Empleado(400, "Anna", Tipo.CONTRATADO, 250.0);
	Tarea t1 = new Tarea(1,"descripción 1", 10); 
	Tarea t2 = new Tarea(2,"descripción 2", 4); 
	Tarea t3 = new Tarea(3,"descripción 3", 2); 
	Tarea t4 = new Tarea(4,"descripción 4", 7); 
	Tarea t5 = new Tarea(5,"descripción 5", 5); 
	Tarea t6 = new Tarea(6,"descripción 6", 3); 
	Tarea t7 = new Tarea(7,"descripción 7", 6); 
	
	@Before
	public void init() throws AsignacionIncorrectaException {
		e1.configurarContratado();
		e2.configurarEfectivo();
		e3.configurarEfectivo();
		e4.configurarContratado();
		e1.getTareasAsignadas().add(t1);
		e1.getTareasAsignadas().add(t2);
		e1.getTareasAsignadas().add(t3);
		e1.getTareasAsignadas().add(t4);
		e1.getTareasAsignadas().add(t5);
		e2.getTareasAsignadas().add(t4); // 7 horas de trabajo estimado
		e2.getTareasAsignadas().add(t5); // 12 horas de trabajo estimado
		e2.getTareasAsignadas().add(t6); // 15 horas de trabajo estimado
		e4.getTareasAsignadas().add(t1);
		e4.getTareasAsignadas().add(t2);
		e4.getTareasAsignadas().add(t7);
	}
	
	//----- Ejercicio 2.a ------
	//Test del método asignarTarea
	@Test
	/** Se le asigna el empleado a la tarea para evitar un NullPointException en el Predicate puedeAsignarTarea
	 *  Como el empleado es contratado y no tiene más de 5 tareas pendientes asignadas, se le puede asignar la tarea
	 */
	public void testAsignarTareaContratadoMenosDe5TareasPendientes() throws AsignacionIncorrectaException {
		t6.setEmpleadoAsignado(e1);
		assertTrue(e1.asignarTarea(t6));
	}
	
	@Test
	/**  Se le asigna el empleado a la tarea para evitar un NullPointException en el Predicate puedeAsignarTarea
	 *  Como el empleado es contratado y tiene más de 5 tareas pendientes asignadas, no se le puede asignar la tarea
	 */
	public void testAsignarTareaContratadoMasde5TareasPendientes() throws AsignacionIncorrectaException {
		t7.setEmpleadoAsignado(e1);
		e1.getTareasAsignadas().add(t6);
		assertFalse(e1.asignarTarea(t7));
	}
	
	@Test
	/** Se le asigna el empleado a la tarea para evitar un NullPointException en el Predicate puedeAsignarTarea
	 *  Como el empleado es efectivo y las tareas pendientes asignadas no suman más de 15 horas
	 *  de trabajo estimado, se le puede asignar la tarea
	 */
	public void testAsignarTareaEfectivo15HorasOMenos() throws AsignacionIncorrectaException {
		t1.setEmpleadoAsignado(e2);
		assertTrue(e2.asignarTarea(t1));
	}
	
	@Test
	/**  Se le asigna el empleado a la tarea para evitar un NullPointException en el Predicate puedeAsignarTarea
	 *  Como el empleado es efectivo y las tareas pendientes asignadas suman más de 15 horas
	 *  de trabajo estimado, no se le puede asignar la tarea
	 */
	public void testAsignarTareaEfectivoMasDe15Horas() throws AsignacionIncorrectaException {
		t1.setEmpleadoAsignado(e2);
		e2.getTareasAsignadas().add(t2); // 19 horas
		assertFalse(e2.asignarTarea(t1));
	}
	
	@Test(expected = AsignacionIncorrectaException.class)
	/** No se puede asignar una tarea que tiene otro empleado asignado. Como el empleado asignado
	 *  no es el mismo al que se le quiere asignar la tarea, entonces ésta no se puede asignar y 
	 *  debe lanzar una AsignacionIncorrectaException.
	 */
	public void testAsignarTareaOtroEmpleadoAsignado() throws AsignacionIncorrectaException {
		t1.setEmpleadoAsignado(e1);
		e2.asignarTarea(t1);
	}
	
	@Test
	/** No se puede asignar una tarea que tiene otro empleado asignado. Como el empleado asignado 
	 *  es el mismo al que se le quiere asignar la tarea, entonces ésta si se puede asignar
	 */
	public void testAsignarTareaMismoEmpleadoAsignado() throws AsignacionIncorrectaException {
		t1.setEmpleadoAsignado(e2);
		assertTrue(e2.asignarTarea(t1));
	}
	
	@Test(expected = AsignacionIncorrectaException.class)
	/** No se puede asignar una tarea que ya fue finalizada, por lo que lanza una 
     *  asignacionIncorrectaException
	 */
	public void testAsignarTareaFinalizada() throws AsignacionIncorrectaException {
		t1.setFechaFin(LocalDateTime.now());
		e2.asignarTarea(t1);
	}
	
	
	//----- Ejercicio 2.b ------
	//Test del método salario
	@Test
	/** Si el empleado no tiene tareas asignadas, el salario debería ser 0
	 */
	public void testSalarioSinTareas() {
		Double esperado = 0.0;
		assertEquals(esperado, e3.salario());
	}
	
	@Test
	/** El empleado e2 tiene 3 tareas asignadas:
	 *  t4, con 7 horas estimadas. Se setea para que termine antes
	 *  t5, con 5 horas estimadas. Se setea para que termine normal
	 *  t6, con 3 horas estimadas. Se setea para que facturada sea true
	 */
	public void testSalarioEfectivo() {
		//Termina antes -> Debe cobrar: 1.2*costoHora*duracionEstimada = 1680
		t4.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t4.setFechaFin(LocalDateTime.of(2020, 9, 2, 22, 0));
		//Termina normal -> Debe cobrar: costoHora*duracionEstimada = 1000
		t5.setFechaInicio(LocalDateTime.of(2020,9,1,22,0));
		t5.setFechaFin(LocalDateTime.of(2020,9,3,22,0));
		//Facturada = true -> No debe tenerse en cuenta
		t6.setFacturada(true);
		Double esperado = 2680.0;
		assertEquals(esperado, e2.salario());
	}
	
	@Test
	/** El empleado e4 tiene 3 tareas asignadas:
	 *  t1, con 10 horas estimadas. Se setea para que termine antes
	 *  t2, con 4 horas estimadas. Se setea para que termine normal
	 *  t7, con 6 horas estimadas. Se setea para que termine atrasada
	 */
	public void testSalarioContratado() {
		//Termina antes -> Debe cobrar: 1.3*costoHora*duracionEstimada = 3250
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 3, 22, 0));
		//Termina normal -> Debe cobrar: costoHora*duracionEstimada = 1000
		t2.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t2.setFechaFin(LocalDateTime.of(2020, 9, 2, 22, 0));
		//Termina atrasada -> Debe cobrar: 0.75*costoHora*duracionEstimada = 1125
		t7.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t7.setFechaFin(LocalDateTime.of(2020, 9, 6, 22, 0));
		Double esperado = 5375.0;
		assertEquals(esperado, e4.salario());
	}

	//Test del método costoTarea
	@Test
	public void testCostoTareaContratadoAdelantado() {
		t7.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t7.setFechaFin(LocalDateTime.of(2020, 9, 2, 22, 0));
		// Horas estimadas: 6, Horas totales: 4
		// -> salario = costoHora*duracionEstimada*1.3
		Double esperado = 1170.0;
		assertEquals(esperado, e1.costoTarea(t7));
	}
	
	@Test
	public void testCostoTareaContratadoNormal() {
		t7.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t7.setFechaFin(LocalDateTime.of(2020, 9, 3, 22, 0));
		// Horas estimadas: 6, Horas totales: 8
		// -> salario = costoHora*duracionEstimada
		Double esperado = 900.0;
		assertEquals(esperado, e1.costoTarea(t7));
	}
	
	@Test
	public void testCostoTareaContratadoAtrasado() {
		t7.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t7.setFechaFin(LocalDateTime.of(2020, 9, 5, 22, 0));
		// Horas estimadas: 6, Horas totales: 16
		// -> salario = costoHora*duracionEstimada*0.75 
		Double esperado = 675.0;
		assertEquals(esperado, e1.costoTarea(t7));
	}
	
	@Test
	public void testCostoTareaEfectivoAdelantado() {
		t7.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t7.setFechaFin(LocalDateTime.of(2020, 9, 2, 22, 0));
		// Horas estimadas: 6, Horas totales: 4
		// -> salario = costoHora*duracionEstimada*1.2
		Double esperado = 1440.0;
		assertEquals(esperado, e2.costoTarea(t7));
	}
	
	@Test
	public void testCostoTareaEfectivoNormal() {
		t7.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
		t7.setFechaFin(LocalDateTime.of(2020, 9, 3, 22, 0));
		// Horas estimadas: 6, Horas totales: 8
		// -> salario = costoHora*duracionEstimada
		Double esperado = 1200.0;
		assertEquals(esperado, e2.costoTarea(t7));
	}


	//----- Ejercicio 2.c ------
	//Test del método comenzar
	@Test(expected = TareaInexistenteException.class)
	/** El empleado e1 tiene las primeras 5 tareas.
	 *  Si buscamos la sexta para comenzarla, deberá ocurrir una excepción.
	 */ 
	public void testComenzarTareaInexistente() throws TareaInexistenteException {
		e1.comenzar(6);
	}
	
	@Test
	/** Como el empleado e1 tiene la tarea t5, al verificar la fecha de ésta última
	 *  debemos obtener la fecha actual
	 */
	public void testComenzarTarea() throws TareaInexistenteException {
		e1.comenzar(5);
		LocalDateTime fecha = LocalDateTime.now();
		assertEquals(fecha, t5.getFechaInicio());
	}
	
	@Test(expected = TareaInexistenteException.class)
	public void testComenzarTareasAsignadasVacia() throws TareaInexistenteException {
		e3.comenzar(1);
	}

	@Test
	@Ignore
	public void testFinalizarInteger() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testComenzarIntegerString() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFinalizarIntegerString() {
		fail("Not yet implemented");
	}

}
