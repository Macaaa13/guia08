package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class AppRRHHTest {

	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	AppRRHH app = new AppRRHH();
	
	@Before
	public void init() {
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

}
