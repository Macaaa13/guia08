package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import org.junit.*;

import java.time.LocalDateTime;

import frsf.isi.died.guia08.problema01.excepciones.*;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class EmpleadoTest {

	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	Empleado e3 = new Empleado(300, "Anna", Tipo.CONTRATADO, 300.0);
	Tarea t1 = new Tarea(1,"Descripción 1", 12); 
	Tarea t2 = new Tarea(2,"Descripción 2", 2); 
	Tarea t3 = new Tarea(3,"Descripción 3", 4); 
	Tarea t4 = new Tarea(4,"Descripción 4", 7); 
	Tarea t5 = new Tarea(5,"Descripción 5", 5); 
	Tarea t6 = new Tarea(6,"Descripción 6", 3);
	
	/** El empleado e1 tiene 4 tareas asignadas, pudiendo tener 5 al mismo tiempo.
	 *  El empleado e2 tiene 1 tarea asignada que tiene 12 de las 15 horas de trabajo estimado que pueden sumar sus tareas.
	 */

	@Before
	public void init(){
		//Empleado e1
		e1.configurarContratado();
		e1.getTareasAsignadas().add(t1);
		e1.getTareasAsignadas().add(t2);
		e1.getTareasAsignadas().add(t3);
		e1.getTareasAsignadas().add(t4);
		//Empleado e2
		e2.configurarEfectivo();
		e2.getTareasAsignadas().add(t1);
		//Empleado e3
		e3.configurarContratado();
	}
	
	//-------------------------
	//----- Ejercicio 2.a -----
	//-------------------------
	
	// Test del método asignarTarea
	/** Se le setea a la tarea t1 el empleado e1, entonces cuando se quiere asignar la tarea al empleado e2 lanza una 
	 *  AsignacionIncorrectaException (no se puede asignar una tarea que ya tiene un empleado asignado).
	 */
	@Test(expected = AsignacionIncorrectaException.class)
	public void testAsignarTareaConEmpleadoYaAsignado() throws AsignacionIncorrectaException {
		t1.setEmpleadoAsignado(e1);
		e2.asignarTarea(t1);
	}
	
	/** Se le setea a la tarea t1 una fecha de finalización, entonces cuando se quiere asignar la tarea al empleado e2
	 *  lanza una AsignacionIncorrectaException (no se puede asignar una tarea que ya ha sido finalizada).
	 */
	@Test(expected = AsignacionIncorrectaException.class)
	public void testAsignarTareaYaFinalizada() throws AsignacionIncorrectaException {
		t1.setFechaFin(LocalDateTime.now());
		e2.asignarTarea(t1);
	}
	
	/** Los empleados efectivos no pueden tener tareas asignadas pendientes que sumen más de 15 horas de trabajo estimado.
	 *  Como el empleado e2 tiene 12 horas de trabajo estimado, si se le asigna la tarea t2 tendrá 14 horas de trabajo estimado, que al ser
	 *  menor que 15 permite que al empleado se le pueda asignar la tarea.
	 */
	@Test
	public void testAsignarTareaEmpleadoEfectivo() throws AsignacionIncorrectaException {
		assertTrue(e2.asignarTarea(t2));
	}

	/** Los empleados efectivos no pueden tener tareas asignadas pendientes que sumen más de 15 horas de trabajo estimado.
	 *  Se agrega la tarea t2 a la lista de tareas asignadas del empleado efectivo e2, sumando un total de 14 horas de trabajo estimado.
	 *  Al intentar asignar la tarea t3 el total de horas de trabajo estimado pasa a ser 18, que al ser mayor que 15 no permite que al
	 *  empleado se le pueda asignar la tarea.
	 */
	@Test
	public void testAsignarTareaEmpleadoEfectivoSinCondiciones() throws AsignacionIncorrectaException {
		e2.getTareasAsignadas().add(t2);
		assertFalse(e2.asignarTarea(t3));
	}
	
	/** Los empleados contratados no pueden tener más de 5 tareas asignadas pendientes.
	 *  Como el empleado e1 tiene 4 tareas asignadas pendientes, se le puede asignar la tarea t5.
	 */
	@Test
	public void testAsignarTareaEmpleadoContratado() throws AsignacionIncorrectaException {
		assertTrue(e1.asignarTarea(t5));
	}
	
	/** Los empleados contratados no pueden tener más de 5 tareas asignadas pendientes.
	 *  Se agrega la tarea t5 a la lista de tareas asignadas del empleado contratado e1, sumando un total de 5 tareas asignadas pendientes.
	 *  Como el empleado e1 ya tiene 5 tareas asignadas pendientes, no se le puede asignar la tarea t6.
	 */
	@Test
	public void testAsignarTareaEmpleadoContratadoSinCondiciones() throws AsignacionIncorrectaException {
		e1.getTareasAsignadas().add(t5);
		assertFalse(e1.asignarTarea(t6));
	}
	
	//-------------------------
	//----- Ejercicio 2.b -----
	//-------------------------
	
	// Test del método costoTarea
	/** Se calcula el costo de la tarea t1 al empleado contratado e3.
	 *  Se setean fecha de inicio y final para que el empleado resuelva la tarea en 12 horas, que al ser igual que la duracion estimada
	 *  hace que el empleado cobre la duración estimada de la tarea multiplicada por el costo hora.
	 */
	@Test
	public void testCostoTareaContratado() throws AsignacionIncorrectaException {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 3, 14, 0));
		Double expected = e3.getCostoHora()*t1.getDuracionEstimada();
		assertEquals(expected, e3.costoTarea(t1));
	}
	
	/** Se calcula el costo de la tarea t1 al empleado contratado e3.
	 *  Se setean fecha de inicio y final para que el empleado resuelva la tarea en 24 horas, que al ser 12 horas más de las estimadas
	 *  (más de 2 días tarde), hace que el empleado cobre un 75% de su valor hora.
	 */
	@Test
	public void testCostoTareaAtrasadaContratado() {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 6, 14, 0));
		Double expected = e3.getCostoHora()*t1.getDuracionEstimada()*0.75;
		assertEquals(expected, e3.costoTarea(t1));
	}
	
	/** Se calcula el costo de la tarea t1 al empleado contratado e3.
	 *  Se setean fecha de inicio y final para que el empleado resuelva la tarea en 8 horas, que al ser 4 horas menos de las estimadas
	 *  (1 día antes), hace que el empleado cobre un 30% de su valor hora.
	 */
	@Test
	public void testCostoTareaAdelantadaContratado() {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 2, 14, 0));
		Double expected = e3.getCostoHora()*t1.getDuracionEstimada()*1.3;
		assertEquals(expected, e3.costoTarea(t1));
	}
	
	/** Se calcula el costo de la tarea t1 al empleado efectivo e2.
	 *  Se setean fecha de inicio y final para que el empleado resuelva la tarea en 12 horas, que al ser igual que la duracion estimada
	 *  hace que el empleado cobre la duración estimada de la tarea multiplicada por el costo hora.
	 */
	@Test
	public void testCostoTareaEfectivo() {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 3, 14, 0));
		Double expected = e2.getCostoHora()*t1.getDuracionEstimada();
		assertEquals(expected, e2.costoTarea(t1));
	}
	
	/** Se calcula el costo de la tarea t1 al empleado efectivo e2.
	 *  Se setean fecha de inicio y final para que el empleado resuelva la tarea en 8 horas, que al ser 4 horas menos de las estimadas
	 *  (1 día antes), hace que el empleado cobre un 20% de su valor hora.
	 */
	@Test
	public void testCostoTareaAdelantadaEfectivo() {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 2, 14, 0));
		Double expected = e2.getCostoHora()*t1.getDuracionEstimada()*1.2;
		assertEquals(expected, e2.costoTarea(t1));
	}
	
	// Test del método salario
	/** Como el empleado e3 no tiene tareas asignadas, su salario debe ser 0
	 */
	@Test
	public void testSalarioSinTareasAsignadas() {
		Double expected = 0.0;
		assertEquals(expected, e3.salario());
	}
	
	/** Se setean las tareas t1 y t2 como facturadas, por lo que el salario del empleado e3 debe ser 0
	 */
	@Test
	public void testSalarioSinTareasNoFacturadas() {
		t1.setFacturada(true);
		t2.setFacturada(true);
		e1.getTareasAsignadas().add(t1);
		e1.getTareasAsignadas().add(t2);
		Double expected = 0.0;
		assertEquals(expected, e3.salario());
	}
	
	/** El empleado e1 tiene las tareas t1, t2, t3 y t4 asignadas y ninguna de ellas fue finalizada, por lo que su salario debe ser 0
	 */
	@Test
	public void testSalarioSinTareasFinalizadas() {
		Double expected = 0.0;
		assertEquals(expected, e1.salario());
	}
	
	/** Se agregan a la lista de tareas asignadas del empleado contratado e3 las tareas t1, t2 y t3.
	 *  Se les setean fecha inicial y final para que:
	 *  - Termine t1 antes: 12 horas estimadas de trabajo, 8 horas reales de trabajo
	 *  - Termine t2 de forma normal: 2 horas estimadas de trabajo, 4 horas reales de trabajo
	 *  - Termine t3 tarde: 4 horas estimadas de trabajo, 20 horas reales de trabajo
	 */
	@Test
	public void testSalarioEmpleadoContratado() throws AsignacionIncorrectaException {
		e3.getTareasAsignadas().add(t1);
		e3.getTareasAsignadas().add(t2);
		e3.getTareasAsignadas().add(t3);
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 2, 14, 0));
		t2.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t2.setFechaFin(LocalDateTime.of(2020, 9, 1, 14, 0));
		t3.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t3.setFechaFin(LocalDateTime.of(2020, 9, 5, 14, 0));
		Double expected = e3.getCostoHora() * (t1.getDuracionEstimada()*1.3 + t2.getDuracionEstimada() + t3.getDuracionEstimada()*0.75);
		assertEquals(expected, e3.salario());
	}
	
	/** Se agrega a la lista de tareas asignadas del empleado efectivo e3 la tarea t2 (ya tiene la tarea t1).
	 *  Se les setean fecha inicial y final para que:
	 *  - Termine t1 antes: 12 horas estimadas de trabajo, 8 horas reales de trabajo
	 *  - Termine t2 de forma normal: 2 horas estimadas de trabajo, 4 horas reales de trabajo
	 */
	@Test
	public void testSalarioEmpleadoEfectivo() throws AsignacionIncorrectaException {
		e2.getTareasAsignadas().add(t2);
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t1.setFechaFin(LocalDateTime.of(2020, 9, 2, 14, 0));
		t2.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0));
		t2.setFechaFin(LocalDateTime.of(2020, 9, 1, 14, 0));
		Double expected = e2.getCostoHora()*t1.getDuracionEstimada()*1.2 + e2.getCostoHora()*t2.getDuracionEstimada();
		assertEquals(expected, e2.salario());
	}
	
	//-------------------------
	//----- Ejercicio 2.c -----
	//-------------------------

	// Test del método comenzar
	/** El empleado e1 tiene las tareas t1, t2, t3 y t4 en su lista de tareas asignadas.
	 *  Si intento comenzar la tarea t5, que no está en la lista, se lanza una TareaInexistenteException.
	 */
	@Test(expected = TareaInexistenteException.class)
	public void testComenzarIntegerTareaInexistente() throws TareaInexistenteException {
		e1.comenzar(5);
	}
	
	/** El empleado e1 tiene las tareas t1, t2, t3 y t4 en su lista de tareas asignadas.
	 *  Si comienzo la tarea t1, se le asigna como fecha de inicio la fecha y hora actual.
	 */
	@Test
	public void testComenzarInteger() throws TareaInexistenteException {
		e1.comenzar(1);
		assertEquals(LocalDateTime.now(), t1.getFechaInicio());
	}
	
	//-------------------------
	//----- Ejercicio 2.d -----
	//-------------------------

	// Test del método finalizar
	/** El empleado e2 tiene la tarea t1 en su lista de tareas asignadas.
	 *  Si intento comenzar la tarea t2, que no está en la lista, se lanza una TareaInexistenteException.
	 */
	@Test(expected = TareaInexistenteException.class)
	public void testFinalizarIntegerTareaInexistente() throws TareaInexistenteException, TareaNoComenzadaException {
		e2.finalizar(2);
	}
	
	/** El empleado e2 tiene la tarea t1 en su lista de tareas asignadas.
	 *  Si intento finalizar la tarea t1 sin haberla comenzado, se lanza una TareaNoComenzadaException.
	 */
	@Test(expected = TareaNoComenzadaException.class)
	public void testFinalizarIntegerTareaNoComenzada() throws TareaInexistenteException, TareaNoComenzadaException {
		e2.finalizar(1);
	}
	
	/** El empleado e2 tiene la tarea t1 en su lista de tareas asignadas, a la cual se le setea una fecha inicial.
	 *  Si finalizo la tarea t1, se le asigna como fecha final la fecha y hora actual.
	 */
	@Test
	public void testFinalizarInteger() throws TareaInexistenteException, TareaNoComenzadaException {
		e2.comenzar(1);
		e2.finalizar(1);
		assertEquals(LocalDateTime.now(), t1.getFechaFin());
	}
	
	//-------------------------
	//----- Ejercicio 2.e -----
	//-------------------------

	// Test del método comenzar sobrecargado
	/** El empleado e1 tiene las tareas t1, t2, t3 y t4 en su lista de tareas asignadas.
	 *  Si intento comenzar la tarea t5, que no está en la lista, se lanza una TareaInexistenteException.
	 * @throws TareaInexistenteException 
	 */
	@Test(expected = TareaInexistenteException.class)
	public void testComenzarIntegerStringTareaInexistente() throws TareaInexistenteException {
		e1.comenzar(5, "1-9-2020 22:00");
	}
	
	/** El empleado e1 tiene las tareas t1, t2, t3 y t4 en su lista de tareas asignadas.
	 *  Si comienzo la tarea t1, se le asigna como fecha de inicio la fecha y hora pasada como parámetro.
	 */
	@Test
	public void testComenzarIntegerString() throws TareaInexistenteException {
		e1.comenzar(1, "01-09-2020 22:00");
		assertEquals(LocalDateTime.parse("2020-09-01T22:00"),t1.getFechaInicio());
	}
	
	//-------------------------
	//----- Ejercicio 2.f -----
	//-------------------------

	// Test del método finalizar sobrecargado
	/** El empleado e2 tiene la tarea t1 en su lista de tareas asignadas.
	 *  Si intento comenzar la tarea t2, que no está en la lista, se lanza una TareaInexistenteException.
	 */
	@Test(expected = TareaInexistenteException.class)
	public void testFinalizarIntegerStringTareaInexistente() throws TareaInexistenteException, TareaNoComenzadaException, TareaFinalizadaAntesDeComenzarException {
		e2.finalizar(2, "03-09-2020 22:00");
	}
	
	/** El empleado e2 tiene la tarea t1 en su lista de tareas asignadas.
	 *  Si intento finalizar la tarea t1 sin haberla comenzado, se lanza una TareaNoComenzadaException.
	 */
	@Test(expected = TareaNoComenzadaException.class)
	public void testFinalizarIntegerStringTareaNoComenzada() throws TareaInexistenteException, TareaNoComenzadaException, TareaFinalizadaAntesDeComenzarException {
		e2.finalizar(1, "03-09-2020 22:00");
	}
	
	/** Se setea la tarea t1 para que comienze el 01-09-2020 a las 22:00.
	 *  Si se trata de finalizar dicha tarea el 29-08-2020, es decir 4 días antes de haberla comenzado, se lanza una 
	 *  TareaFinalizadaAntesDeComenzarException.
	 * @throws TareaInexistenteException 
	 * @throws TareaFinalizadaAntesDeComenzarException 
	 * @throws TareaNoComenzadaException 
	 */
	@Test(expected = TareaFinalizadaAntesDeComenzarException.class)
	public void testFinalizarIntegerStringTareaFinalizadaAntesDeComenzar() throws TareaInexistenteException, TareaNoComenzadaException, TareaFinalizadaAntesDeComenzarException {
		e2.comenzar(1, "01-09-2020 22:00");
		e2.finalizar(1, "29-08-2020 22:00");
	}
	
	/** El empleado e2 tiene la tarea t1 en su lista de tareas asignadas.
	 *  Se comienza la tarea el 01-09-2020 a las 22:00 y luego se finaliza el 03-09-2020 a las 22:00.
	 */
	@Test
	public void testFinalizarIntegerString() throws TareaInexistenteException, TareaNoComenzadaException, TareaFinalizadaAntesDeComenzarException {
		e2.comenzar(1, "01-09-2020 22:00");
		e2.finalizar(1, "03-09-2020 22:00");
		assertEquals(LocalDateTime.parse("2020-09-03T22:00"), t1.getFechaFin());
	}
	
	
}
