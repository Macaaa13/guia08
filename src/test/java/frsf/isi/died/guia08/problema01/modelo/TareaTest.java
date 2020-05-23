package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {

	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	Tarea t1 = new Tarea(1,"descripción 1", 12);
	
	@Before
	public void init() {
		t1.setFechaInicio(LocalDateTime.of(2020, 9, 1, 22, 0));
	}
	
	//Ejercicio 2.b
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
	
	@Test
	@Ignore
	public void asignarEmpleadoTest() {
		fail("Not yet implemented");
	}

}
