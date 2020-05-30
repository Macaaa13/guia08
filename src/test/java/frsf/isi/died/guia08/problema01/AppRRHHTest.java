package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import org.junit.Test;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.excepciones.*;
import frsf.isi.died.guia08.problema01.modelo.*;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class AppRRHHTest {

	Empleado e1 = new Empleado(100, "Juan", Tipo.CONTRATADO, 150.0);
	Empleado e2 = new Empleado(200, "Marta", Tipo.EFECTIVO, 200.0);
	Empleado e3 = new Empleado(300, "Anna", Tipo.CONTRATADO, 300.0);
	Empleado e4 = new Empleado(400, "Matt", Tipo.EFECTIVO, 200.0);
	Tarea t1 = new Tarea(1,"Descripción 1", 12); 
	Tarea t2 = new Tarea(2,"Descripción 2", 2); 
	Tarea t3 = new Tarea(3,"Descripción 3", 4); 
	Tarea t4 = new Tarea(4,"Descripción 4", 7); 
	Tarea t5 = new Tarea(5,"Descripción 5", 5); 
	AppRRHH app = new AppRRHH();

	//-------------------------
	//----- Ejercicio 4.a -----
	//-------------------------
	
	// Test del método agregarEmpleadoContratado
	/** El empleado agregado debe aparecer en la lista de empleados.
	 */
	@Test
	public void testAgregarEmpleadoContratado() {
		app.agregarEmpleadoContratado(100, "Juan", 150.0);
		assertTrue(app.getEmpleados().contains(e1));
	}
	
	//-------------------------
	//----- Ejercicio 4.b -----
	//-------------------------
	
	// Test del método agregarEmpleadoEfectivo
	/** El empleado agregado debe aparecer en la lista de empleados.
	 */
	@Test
	public void testAgregarEmpleadoEfectivo() {
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		assertTrue(app.getEmpleados().contains(e2));
	}
	
	//-------------------------
	//----- Ejercicio 4.c -----
	//-------------------------
	
	// Test del método asignarTarea
	/** Como el empleado no está en la lista de empleados, no se le puede asignar la tarea y se lanza una EmpleadoInexistenteException.
	 */
	@Test(expected = EmpleadoInexistenteException.class)
	public void testAsignarTareaAEmpleadoInexistente() throws AsignacionIncorrectaException, EmpleadoInexistenteException {
		app.asignarTarea(100, 1, "Descripción 1", 12);
	}
	
	/** El empleado está en la lista de empleados, pero la tarea que se le quiere asignar ya tiene un empleado asignado, por lo que se
	 *  lanza una AsignacionIncorrectaException.
	 */
	@Test(expected = AsignacionIncorrectaException.class)
	public void testAsignarTareaConEmpleadoYaAsignado() throws AsignacionIncorrectaException, EmpleadoInexistenteException {
		app.agregarEmpleadoContratado(100, "Juan", 150.0);
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		app.asignarTarea(100, 1, "Descripción 1", 12);
		app.asignarTarea(200, 1, "Descripción 1", 12);
	}
	
	/** El empleado contratado se agrega a la lista de empleados y se le asignan 5 tareas.
	 *  Al intentas asignar una sexta se lanza una AsignacionIncorrectaException, ya que un empleado contratado no puede tener más
	 *  de 5 tareas asignadas pendientes. 
	 */
	@Test(expected = AsignacionIncorrectaException.class)
	public void testAsignarTareaAEmpleadoContratadoSinCondicionesNecesarias() throws AsignacionIncorrectaException, EmpleadoInexistenteException {
		app.agregarEmpleadoContratado(100, "Juan", 150.0);
		app.asignarTarea(100, 1, "Descripción 1", 12);
		app.asignarTarea(100, 2, "Descripción 2", 2);
		app.asignarTarea(100, 3, "Descripción 3", 4);
		app.asignarTarea(100, 4, "Descripción 4", 7);
		app.asignarTarea(100, 5, "Descripción 5", 5);
		app.asignarTarea(100, 6, "Descripción 6", 3); // AsignacionIncorrectaException
	}
	
	/** El empleado efectivo se agrega a la lista de empleados y se le asignan 1 tarea con 12 horas estimadas de trabajo.
	 *  Al intentar asignar una segunda tarea con 5 horas estimadas de trabajo, el total de horas estimadas pasa a ser 17 y se lanza
	 *  una AsignacionIncorrectaException, ya que los empleados efectivos no pueden tener tareas asignadas pendientes que sumen más
	 *  de 15 horas estimadas de trabajo.
	 */
	@Test(expected = AsignacionIncorrectaException.class)
	public void testAsignarTareaAEmpleadoEfectivoSinCondicionesNecesarias() throws AsignacionIncorrectaException, EmpleadoInexistenteException {
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		app.asignarTarea(200, 1, "Descripción 1", 12);
		app.asignarTarea(200, 5, "Descripción 5", 5); // AsignacionIncorrectaException
	}
	
	/** El empleado efectivo se agrega a la lista de empleados.
	 *  Se le asigna una tarea, la cual debe aparecer dentro de las tareas asignadas del empleado.
	 */
	@Test
	public void testAsignarTarea() throws AsignacionIncorrectaException, EmpleadoInexistenteException {
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		app.asignarTarea(200, 1, "Descripción 1", 12);
		List<Tarea> lista = app.getEmpleados().stream()
						  .filter(e -> e.getCuil() == 200)
						  .map(e -> e.getTareasAsignadas())
						  .flatMap(List::stream)
						  .collect(Collectors.toList());
		assertTrue(lista.contains(t1));
	}
	
	//-------------------------
	//----- Ejercicio 4.d -----
	//-------------------------
	
	// Test del método empezarTarea
	/** Como el empleado no está en la lista de empleados, no se puede comenzar la tarea y se lanza una 
	 *  EmpleadoInexistenteException.
	 */
	@Test(expected = EmpleadoInexistenteException.class)
	public void testEmpezarTareaConEmpleadoInexistente() throws TareaInexistenteException, EmpleadoInexistenteException {
		app.empezarTarea(100, 1);
	}
	
	/** El empleado está en la lista de empleados pero no fue asignado a la tarea con id 1, por lo que se lanza una
	 *  TareaInexistenteException
	 */
	@Test(expected = TareaInexistenteException.class)
	public void testEmpezarTareaInexistente()  throws TareaInexistenteException, EmpleadoInexistenteException {
		app.agregarEmpleadoContratado(100, "Juan", 150.0);
		app.empezarTarea(100, 1);
	}
	
	/** Se agrega el empleado contratado a la lista de empleados y se le asigna una tarea.
	 *  Luego ésta es comenzada, por lo que se le setea como fecha de inicio la fecha y hora actual.
	 *  Entonces si se busca en la lista de empledos al empleado y se extraen sus tareas asignadas, debe haber una que
	 *  tenga el mismo id que la tarea asignada, la cual tiene una fecha de inicio no vacía. 
	 */
	@Test
	public void testEmpezarTarea() throws AsignacionIncorrectaException, EmpleadoInexistenteException, TareaInexistenteException {
		app.agregarEmpleadoContratado(100, "Juan", 150.0);
		app.asignarTarea(100, 1, "Descripción 1", 12);
		app.empezarTarea(100, 1);
		List<Tarea> lista = app.getEmpleados().stream()
				  .filter(e -> e.getCuil() == 100)
				  .map(e -> e.getTareasAsignadas())
				  .flatMap(List::stream)
				  .collect(Collectors.toList());
		assertTrue( lista.stream().filter(t -> t.getId() == 1 && t.getFechaInicio() != null).count() == 1 );
	}
	
	//-------------------------
	//----- Ejercicio 4.e -----
	//-------------------------
	
	// Test del método terminarTarea
	/** Como el empleado no está en la lista de empleados, no se finalizar la tarea y se lanza una 
	 *  EmpleadoInexistenteException.
	 */
	@Test(expected = EmpleadoInexistenteException.class)
	public void testTerminarTareaConEmpleadoInexistente() throws TareaInexistenteException, EmpleadoInexistenteException, TareaNoComenzadaException {
		app.terminarTarea(100, 1);
	}
	
	/** El empleado está en la lista de empleados pero no fue asignado a la tarea con id 4, por lo que se lanza una
	 *  TareaInexistenteException
	 */
	@Test(expected = TareaInexistenteException.class)
	public void testTerminarTareaInexistente() throws TareaInexistenteException, TareaNoComenzadaException, EmpleadoInexistenteException {
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		app.terminarTarea(200, 4);
	}
	
	/** El empleado está en la lista de empleados y tiene la tarea asignada, pero como no fue comenzada no puede terminarse
	 *  y se lanza una TareaNoComenzadaException.
	 */
	@Test(expected = TareaNoComenzadaException.class)
	public void testTerminarTareaNoComenzada() throws AsignacionIncorrectaException, EmpleadoInexistenteException, TareaInexistenteException, TareaNoComenzadaException {
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		app.asignarTarea(200, 4, "Descripción 4", 7);
		app.terminarTarea(200, 4);
	}
	
	/** El empleado está en la lista de empleados, tiene la tarea asignada y esta fue comenzada, por lo que es posible terminarla.
	 *  Entonces si se busca en la lista de empledos al empleado y se extraen sus tareas asignadas, debe haber una que
	 *  tenga el mismo id que la tarea asignada, la cual tiene una fecha final no vacía. 
	 */
	@Test
	public void testTerminarTarea() throws AsignacionIncorrectaException, EmpleadoInexistenteException, TareaInexistenteException, TareaNoComenzadaException {
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		app.asignarTarea(200, 4, "Descripción 4", 7);
		app.empezarTarea(200, 4);
		app.terminarTarea(200, 4);
		List<Tarea> lista = app.getEmpleados().stream()
				  .filter(e -> e.getCuil() == 200)
				  .map(e -> e.getTareasAsignadas())
				  .flatMap(List::stream)
				  .collect(Collectors.toList());
		assertTrue( lista.stream().filter(t -> t.getId() == 4 && t.getFechaFin() != null).count() == 1 );
	}
	
	//-------------------------
	//----- Ejercicio 4.f -----
	//-------------------------
	
	// Test del método cargarEmpleadosContratadosCSV
	/** En el archivo empleados.csv se cargan a modo de ejemplo 2 empleados:
	 *  100;Juan;150.0
	 *	300;Anna;300.0
	 *  Entonces cuando se llama al método cargarEmpleadosContratadosCSV estos se cargan en la lista de empleados.
	 */
	@Test
	public void testCargarEmpleadosContratadosCSV() throws IOException {
		try(Writer fileWriter = new FileWriter("empleados.csv")){
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				out.write(100+";Juan;"+150.0+System.getProperty("line.separator"));
				out.write(300+";Anna;"+300.0+System.getProperty("line.separator"));
			}
		}
		app.cargarEmpleadosContratadosCSV("empleados.csv");
		assertTrue( app.getEmpleados().contains(e1) && app.getEmpleados().contains(e3) );
	}
	
	/** El archivo empleadosInexistente.csv no existe, por lo que se lanza una FileNotFoundException.
	 */
	@Test(expected = FileNotFoundException.class)
	public void testCargarEmpleadosContratadosCSVArchivoInexistente() throws FileNotFoundException, IOException {
		app.cargarEmpleadosContratadosCSV("empleadosInexistente.csv");
	}
	
	//-------------------------
	//----- Ejercicio 4.g -----
	//-------------------------
	
	// Test del método cargarEmpleadosEfectivosCSV
	/** En el archivo empleados.csv se cargan a modo de ejemplo 2 empleados:
	 *  200;Marta;200.0
	 *	400;Matt;200.0
	 *  Entonces cuando se llama al método cargarEmpleadosEfectivosCSV estos se cargan en la lista de empleados.
	 */
	@Test
	public void testCargarEmpleadosEfectivosCSV() throws IOException {
		try(Writer fileWriter = new FileWriter("empleados.csv")){
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				out.write(200+";Marta;"+200.0+System.getProperty("line.separator"));
				out.write(400+";Matt;"+200.0+System.getProperty("line.separator"));
			}
		}
		app.cargarEmpleadosEfectivosCSV("empleados.csv");
		assertTrue( app.getEmpleados().contains(e2) && app.getEmpleados().contains(e4) );
	}
	
	/** El archivo empleadosInexistente.csv no existe, por lo que se lanza una FileNotFoundException.
	 */
	@Test(expected = FileNotFoundException.class)
	public void testCargarEmpleadosEfectivosCSVArchivoInexistente() throws FileNotFoundException, IOException {
		app.cargarEmpleadosEfectivosCSV("empleadosInexistente.csv");
	}
	
	//-------------------------
	//----- Ejercicio 4.h -----
	//-------------------------
	
	// Test del método cargarTareasCSV
	/** En el archivo tareas.csv se carga a modo de ejemplo:
	 *  100;1;Descripción 1;12
	 *  Es decir, la tarea con id 1 tiene asignada al empleado 100. Al llamar al método cargarTareasCSV se carga dicha tarea
	 *  al empleado, pero como el empleado no está en la lista de empleados no se le puede cargar la tarea.
	 */ 
	@Test(expected = EmpleadoInexistenteException.class)
	public void testCargarTareasCSVEmpleadoInexistente() throws IOException, AsignacionIncorrectaException, EmpleadoInexistenteException {
		try(Writer fileWriter = new FileWriter("tareas.csv")){
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				out.write(100+";"+1+";Descripción 1;"+12+System.getProperty("line.separator"));
			}
		}
		app.cargarTareasCSV("tareas.csv");
	}
	
	/** En el archivo tareas.csv se carga a modo de ejemplo:
	 *  100;1;Descripción 1;12
	 *  Es decir, la tarea con id 1 tiene asignada al empleado 100. Al llamar al método cargarTareasCSV se carga dicha tarea
	 *  al empleado, pero como este no cumple las condiciones necesarias para la asignación se lanza una AsignacionIncorrectaException.
	 */  
	@Test(expected = AsignacionIncorrectaException.class)
	public void testCargarTareasCSVEmpleadoSinCondiciones() throws AsignacionIncorrectaException, EmpleadoInexistenteException, IOException {
		app.agregarEmpleadoContratado(100, "Juan", 150.0);
		app.asignarTarea(100, 1, "Descripción 1", 12);
		app.asignarTarea(100, 2, "Descripción 2", 2);
		app.asignarTarea(100, 3, "Descripción 3", 4);
		app.asignarTarea(100, 4, "Descripción 4", 7);
		app.asignarTarea(100, 5, "Descripción 5", 5);
		try(Writer fileWriter = new FileWriter("tareas.csv")){
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				out.write(100+";"+6+";Descripción 6;"+3+System.getProperty("line.separator"));
			}
		}
		app.cargarTareasCSV("tareas.csv");
	}
	
	/** En el archivo tareas.csv se carga a modo de ejemplo:
	 *  100;1;Descripción 1;12
	 *  100;2;Descripción 2;2
	 *  100;3;Descripción 3;4
	 *  300;4;Descripción 4;7
	 *  Entonces el empleado con cuil 100 debe tener la tarea con id 1, el emplado con cuil 200 debe tener la tareas con id
	 *  2 y 3, y el empleado con cuil 300 debe tener la tarea con id 4.
	 */  
	@Test
	public void testCargarTareasCSV() throws IOException, AsignacionIncorrectaException, EmpleadoInexistenteException {
		app.agregarEmpleadoContratado(100, "Juan", 150.0);
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		app.agregarEmpleadoContratado(300, "Anna", 300.0);
		try(Writer fileWriter = new FileWriter("tareas.csv")){
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				out.write(100+";"+1+";Descripción 1;"+12+System.getProperty("line.separator"));
				out.write(200+";"+2+";Descripción 2;"+2+System.getProperty("line.separator"));
				out.write(200+";"+3+";Descripción 3;"+4+System.getProperty("line.separator"));
				out.write(300+";"+4+";Descripción 4;"+7+System.getProperty("line.separator"));
			}
		}
		app.cargarTareasCSV("tareas.csv");
		List<Tarea> lista1 = app.getEmpleados().stream()
				  .filter(e -> e.getCuil() == 100)
				  .map(e -> e.getTareasAsignadas())
				  .flatMap(List::stream)
				  .collect(Collectors.toList());
		List<Tarea> lista2 = app.getEmpleados().stream()
				  .filter(e -> e.getCuil() == 200)
				  .map(e -> e.getTareasAsignadas())
				  .flatMap(List::stream)
				  .collect(Collectors.toList());
		List<Tarea> lista3 = app.getEmpleados().stream()
				  .filter(e -> e.getCuil() == 300)
				  .map(e -> e.getTareasAsignadas())
				  .flatMap(List::stream)
				  .collect(Collectors.toList());
		assertTrue( lista1.contains(t1) &&
				    lista2.contains(t2) && lista2.contains(t3) &&
				    lista3.contains(t4));
	}
	
	//-------------------------
	//----- Ejercicio 4.h -----
	//-------------------------
	
	// Test del método facturar
	/** Como la lista de empleados está vacía, no hay tareas que puedan ser facturadas y el total a facturar es 0.0
	 */
	@Test
	public void testFacturarListaDeEmpleadosVacia() throws IOException {
		Double expected = 0.0;
	    assertEquals(expected, app.facturar());
	}
	
	/** La lista de empleados no está vacía, pero ninguno tiene tareas asignadas, por lo que el total a facturar es 0.0
	 */
    @Test
    public void testFacturarEmpleadosSinTareasAsignadas() throws IOException {
    	app.agregarEmpleadoContratado(100, "Juan", 150.0);
    	app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
    	Double expected = 0.0;
	    assertEquals(expected, app.facturar());
    }
    
    /** La lista de empleados no está vacía y los empleados tienen tareas asignadas, pero ninguna fue finalizada por lo que
	 *  el total a facturar es 0.0 
	 */
    @Test
    public void testFacturarEmpleadosSinTareasTerminadas() throws AsignacionIncorrectaException, EmpleadoInexistenteException, IOException {
    	app.agregarEmpleadoContratado(100, "Juan", 150.0);
    	app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
    	app.asignarTarea(100, 1, "Descripción 1", 12);
    	app.asignarTarea(200, 2, "Descripción 2", 2);
    	Double expected = 0.0;
	    assertEquals(expected, app.facturar());
    }
    
    /** La lista de empleados no está vacía y estos tienen tareas asignadas que ya fueron finalizadas y facturadas,
	 *  por lo que no se consideran en el cálculo y el total a facturar es 0.0
	 */
    @Test
    public void testFacturarEmpleadosConTareasYaFacturadas() throws AsignacionIncorrectaException, EmpleadoInexistenteException, TareaInexistenteException, TareaNoComenzadaException, IOException {
    	app.agregarEmpleadoContratado(100, "Juan", 150.0);
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		app.asignarTarea(100, 1, "Descripción 1", 12);
		app.asignarTarea(200, 2, "Descripción 2", 7);
		app.empezarTarea(100, 1);
		app.terminarTarea(100, 1);
		app.empezarTarea(200, 2);
		app.terminarTarea(200, 2);
		app.getEmpleados().stream().map(e -> e.getTareasAsignadas()).flatMap(List::stream).forEach(t -> t.setFacturada(true));
		Double expected = 0.0;
		assertEquals(expected,app.facturar());
    }
    
    /** Como no se puede especificar las fechas de inicio y fin de una tarea, ya que asigna fecha y hora actual, se buscan las
     *  tareas en la lista de empleados y se setean.
     *  Para el empleado contratado se les setean fecha inicial y final para que:
	 *  - Termine t1 antes: 12 horas estimadas de trabajo, 8 horas reales de trabajo
	 *  - Termine t2 de forma normal: 2 horas estimadas de trabajo, 4 horas reales de trabajo
	 *  - Termine t3 tarde: 4 horas estimadas de trabajo, 20 horas reales de trabajo
	 *  
	 *  Para el empleado efectivo se les setean fecha inicial y final para que:
	 *  - Termine t4 antes: 7 horas estimadas de trabajo, 4 horas reales de trabajo
	 *  - Termine t5 de forma normal: 5 horas estimadas de trabajo, 8 horas reales de trabajo
     */
    @Test
    public void testFacturar() throws AsignacionIncorrectaException, EmpleadoInexistenteException, IOException {
    	app.agregarEmpleadoContratado(100, "Juan", 150.0);
		app.agregarEmpleadoEfectivo(200, "Marta", 200.0);
		app.asignarTarea(100, 1, "Descripción 1", 12);
		app.asignarTarea(100, 2, "Descripción 2", 2);
		app.asignarTarea(100, 3, "Descripción 3", 4);
		app.asignarTarea(200, 4, "Descripción 4", 7);
		app.asignarTarea(200, 5, "Descripción 5", 5);
		app.getEmpleados().stream().filter(e -> e.getCuil()==100)
		 						   .map(e -> e.getTareasAsignadas())
		 						   .flatMap(List::stream)
		 						   .filter(t -> t.getId()==1)
		 						   .forEach(t -> {t.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0)); 
		 								         t.setFechaFin(LocalDateTime.of(2020, 9, 2, 14, 0));
		 								         });
		app.getEmpleados().stream().filter(e -> e.getCuil()==100)
		   						   .map(e -> e.getTareasAsignadas())
		   						   .flatMap(List::stream)
		   						   .filter(t -> t.getId()==2)
		   						   .forEach(t -> {t.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0)); 
		   						   				  t.setFechaFin(LocalDateTime.of(2020, 9, 1, 14, 0));
		   						   				 });
		app.getEmpleados().stream().filter(e -> e.getCuil()==100)
								   .map(e -> e.getTareasAsignadas())
								   .flatMap(List::stream)
								   .filter(t -> t.getId()==3)
								   .forEach(t -> {t.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0)); 
								   				  t.setFechaFin(LocalDateTime.of(2020, 9, 5, 14, 0));
								   				 });
		app.getEmpleados().stream().filter(e -> e.getCuil()==200)
		   						   .map(e -> e.getTareasAsignadas())
		   						   .flatMap(List::stream)
		   						   .filter(t -> t.getId()==4)
		   						   .forEach(t -> {t.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0)); 
		   						   				  t.setFechaFin(LocalDateTime.of(2020, 9, 1, 14, 0));
		   						   				 });
		app.getEmpleados().stream().filter(e -> e.getCuil()==200)
		   						   .map(e -> e.getTareasAsignadas())
		   						   .flatMap(List::stream)
		   						   .filter(t -> t.getId()==5)
		   						   .forEach(t -> {t.setFechaInicio(LocalDateTime.of(2020, 9, 1, 10, 0)); 
		   						   				  t.setFechaFin(LocalDateTime.of(2020, 9, 2, 14, 0));
		   						   				 });
		Double expected = e1.getCostoHora() * (t1.getDuracionEstimada()*1.3 + t2.getDuracionEstimada() + t3.getDuracionEstimada()*0.75)
				          + e2.getCostoHora()*t4.getDuracionEstimada()*1.2 + e2.getCostoHora()*t5.getDuracionEstimada();
		assertEquals(expected, app.facturar());
    }
}
