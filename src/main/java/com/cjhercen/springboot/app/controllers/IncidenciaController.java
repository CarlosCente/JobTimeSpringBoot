package com.cjhercen.springboot.app.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.models.service.interfaces.IEmpleadoService;
import com.cjhercen.springboot.app.models.service.interfaces.IFichajeService;
import com.cjhercen.springboot.app.models.service.interfaces.IIncidenciaService;
import com.cjhercen.springboot.app.util.ConstantesUtils;
import com.cjhercen.springboot.app.util.FechaUtils;

@Controller
public class IncidenciaController {

	@Autowired
	IIncidenciaService incidenciaService;
	
	@Autowired
	IEmpleadoService empleadoService;
	
	@Autowired
	UsuarioServiceImpl usuarioService;
	
	@Autowired
	IFichajeService fichajeService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@RequestMapping(value = "/incidencias")
	public String crear(Map<String, Object> model) {

		ArrayList<Incidencia> incidenciasPresentes = (ArrayList<Incidencia>) incidenciaService.findAll();
		int totalErrores = calcularIncidenciasErrores(incidenciasPresentes);
		int totalAdvertencias = calcularIncidenciasAdvertencias(incidenciasPresentes);
		int totalSolucionadas = calcularIncidenciasSolucionadas(incidenciasPresentes);
		int totalAbiertas = calcularIncidenciasAbiertas(incidenciasPresentes);
			
		model.put("incidencias", incidenciasPresentes);
		model.put("totalErrores", totalErrores);
		model.put("totalAdvertencias", totalAdvertencias);
		model.put("totalSolucionadas", totalSolucionadas);
		model.put("totalAbiertas", totalAbiertas);
		model.put("titulo", "Incidencias de los empleados");
		return "incidencias";
	}
	
	@RequestMapping(value = "/incidencias/eliminar")
	public String eliminarIncidencia(
			@RequestParam(value = "username") String username ,
			@RequestParam(value = "fecha") String fecha,
			@RequestParam(value = "mensaje") String mensaje) {

		Date fechaIncidencia = fechaUtils.obtenerFechaApartirString(fecha);
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();
		Incidencia incidenciaABorrar = incidenciaService.
				findByEmpleadoAndFechaAndMensaje(empleado, fechaIncidencia, mensaje);
		
		incidenciaService.delete(incidenciaABorrar);

		log.info("Se ha borrado correctamente la incidencia " + incidenciaABorrar.toString());

		return "redirect:/incidencias";
	}
	
	@RequestMapping(value = "/incidencias/descripcionIncidencia/{mensaje}/{cod_empl}/{fecha}")
	public String getDescripcionIncidencia(Map<String, Object> model, 
			@PathVariable(value = "mensaje") String mensaje,
			@PathVariable(value = "cod_empl") Long cod_empl ,
			@PathVariable(value = "fecha") String fecha) {
		
		Date fechaIncidencia = fechaUtils.obtenerFechaApartirString(fecha);
		Empleado empleado = empleadoService.findOne(cod_empl);
		Incidencia incidenciaAMostrar = incidenciaService.
				findByEmpleadoAndFechaAndMensaje(empleado, fechaIncidencia, mensaje);
		
		//Valores a recuperar de la descripcion generada por la incidencia de entrada o salida
		if(ConstantesUtils.INCIDENCIA_FICHAJE_ENTRADA.equals(incidenciaAMostrar.getMensaje()) || 
				ConstantesUtils.INCIDENCIA_FICHAJE_SALIDA.equals(incidenciaAMostrar.getMensaje())) {
			String fechaDescripcion = recuperarFechaDescripcion(incidenciaAMostrar);
			String horaDescripcion = recuperarHoraDescripcion(incidenciaAMostrar);
			model.put("fechaDescripcion", fechaDescripcion);
			model.put("horaDescripcion", horaDescripcion);
			
		}
		
		//Valores a recuperar de la descripción generada por la incidencia de datos personales
		if(ConstantesUtils.INCIDENCIA_PERFIL.equals(incidenciaAMostrar.getMensaje())) {
			ArrayList<Map<String, String>> listaValoresModificados = new ArrayList<Map<String, String>>();
			recuperarCamposConIncidencia(incidenciaAMostrar, listaValoresModificados);
			model.put("camposIncidencia", listaValoresModificados);
		}
		
		if(ConstantesUtils.INCIDENCIA_DATOS_DIRECCION.equals(incidenciaAMostrar.getMensaje())) {
			ArrayList<Map<String, String>> listaValoresModificados = new ArrayList<Map<String, String>>();
			recuperarCamposConIncidencia(incidenciaAMostrar, listaValoresModificados);
			model.put("camposIncidencia", listaValoresModificados);
		}
		
		String descripcionFormateada = incidenciaAMostrar.getDescripcion().replaceAll("\\(", "").replaceAll("\\)", "");
		
		model.put("descripcionFormateada", descripcionFormateada);
		model.put("incidencia",incidenciaAMostrar);
		model.put("titulo", "Descripción de la Incidencia");
		
		return "descripcionIncidencia";
	}
	
	@RequestMapping(value = "/incidencias/descripcionIncidencia/resolver/{mensaje}/{cod_empl}/{fecha}")
	public String resolverIncidencia (Map<String, Object> model,
			@PathVariable(value = "mensaje") String mensaje,
			@PathVariable(value = "cod_empl") Long cod_empl ,
			@PathVariable(value = "fecha") String fecha, RedirectAttributes flash) {
			
		Date fechaIncidencia = fechaUtils.obtenerFechaApartirString(fecha);
		Empleado empleado = empleadoService.findOne(cod_empl);
		Incidencia incidencia = incidenciaService.findByEmpleadoAndFechaAndMensaje(empleado, fechaIncidencia, mensaje);
		
		//Casos que se resuelven automáticamente, la incidencia de tipo otros se soluciona manualmente
		if(mensaje.equals(ConstantesUtils.INCIDENCIA_FICHAJE_ENTRADA)) {
			
			//Cambio de la hora de entrada del día indicado en la descripcion
			//Primero se obtiene la hora de entrada y el dia
			String fechaDescripcion = recuperarFechaDescripcion(incidencia);
			Date fechaConvertida = fechaUtils.obtenerFechaApartirStringFormato2(fechaDescripcion);
			String horaDescripcion = recuperarHoraDescripcion(incidencia);

			Fichaje fichaje = fichajeService.findByEmpleadoAndFecha(empleado, fechaConvertida);
			
			//Se comprueba si existe el fichaje de ese dia
			if(fichaje != null) {
			
				fichaje.setHoraEntrada(horaDescripcion);
				
				//Se marca la incidencia como resuelta
				incidencia.setEstado(ConstantesUtils.INCIDENCIA_RESUELTA);
				incidenciaService.save(incidencia);
				
				//Se calcula nuevamente el tiempo total
				String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichaje.getHoraEntrada(), fichaje.getHoraSalida());
				fichaje.setTiempoTotal(fechaUtils.formatearFechas2digitos(totalTiempo));
				
				fichajeService.save(fichaje);
				log.info("Se ha resuelto correctamente la incidencia " + incidencia.toString());
				flash.addFlashAttribute("tipo", "Información");
			
			} else {
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "No existe el fichaje del empleado para el día que se indica en la incidencia");
				log.info("No existe el fichaje del empleado "+ empleado.getNombre() + " " + empleado.getApellido1() + " " + 
							empleado.getApellido2() +" para el día " + fechaDescripcion);				
			}
			
		} else if (mensaje.equals(ConstantesUtils.INCIDENCIA_FICHAJE_SALIDA)) {
			
			//Cambio de la hora de salida del día indicado en la descripcion
			//Primero se obtiene la hora de salida y el dia
			String fechaDescripcion = recuperarFechaDescripcion(incidencia);
			Date fechaConvertida = fechaUtils.obtenerFechaApartirStringFormato2(fechaDescripcion);
			String horaDescripcion = recuperarHoraDescripcion(incidencia);

			Fichaje fichaje = fichajeService.findByEmpleadoAndFecha(empleado, fechaConvertida);
			
			//Se comprueba si existe el fichaje de ese dia
			if(fichaje != null) {
					
				fichaje.setHoraSalida(horaDescripcion);
				
				//Se marca la incidencia como resuelta
				incidencia.setEstado(ConstantesUtils.INCIDENCIA_RESUELTA);
				incidenciaService.save(incidencia);
				
				//Se calcula nuevamente el tiempo total
				String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichaje.getHoraEntrada(), fichaje.getHoraSalida());
				fichaje.setTiempoTotal(fechaUtils.formatearFechas2digitos(totalTiempo));
				
				fichajeService.save(fichaje);
				
				log.info("Se ha resuelto correctamente la incidencia " + incidencia.toString());
				flash.addFlashAttribute("tipo", "Información");
				
			} else {
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "No existe el fichaje del empleado para el día que se indica en la incidencia");
				log.info("No existe el fichaje del empleado "+ empleado.getNombre() + " " + empleado.getApellido1() + " " + 
						empleado.getApellido2() +" para el día " + fechaDescripcion);	
			}
			
		} else if (mensaje.equals(ConstantesUtils.INCIDENCIA_DATOS_DIRECCION)) {
			
			//Esta incidencia simplemente es informativa, se marcará como resuelta y ya está
			incidencia.setEstado(ConstantesUtils.INCIDENCIA_RESUELTA);
			incidenciaService.save(incidencia);
			log.info("Se ha resuelto correctamente la incidencia " + incidencia.toString());
			flash.addFlashAttribute("tipo", "Información");
			
		} else if (mensaje.equals(ConstantesUtils.INCIDENCIA_PERFIL)) {
			
			ArrayList<Map<String, String>> listaValoresModificados = new ArrayList<Map<String, String>>();
			recuperarCamposConIncidencia(incidencia, listaValoresModificados);
			
			//Modificar los campos que se han recogido en la incidencia para modificar
			for(int x=0; x < listaValoresModificados.size() ; x++) {
				
				if(listaValoresModificados.get(x).containsKey(ConstantesUtils.CAMPO_NOMBRE)){
					empleado.setNombre(listaValoresModificados.get(x).get(ConstantesUtils.CAMPO_NOMBRE));
				}
				
				if(listaValoresModificados.get(x).containsKey(ConstantesUtils.CAMPO_APELLIDO1)){
					empleado.setApellido1(listaValoresModificados.get(x).get(ConstantesUtils.CAMPO_APELLIDO1));
				}
				
				if(listaValoresModificados.get(x).containsKey(ConstantesUtils.CAMPO_APELLIDO2)){
					empleado.setApellido2(listaValoresModificados.get(x).get(ConstantesUtils.CAMPO_APELLIDO2));
				}
				
				if(listaValoresModificados.get(x).containsKey(ConstantesUtils.CAMPO_FECHANACIM)){
					String fechaNacim = listaValoresModificados.get(x).get(ConstantesUtils.CAMPO_FECHANACIM);
					Date fechaConvertida = fechaUtils.obtenerFechaApartirStringFormato3(fechaNacim);
					empleado.setFechaNacim(fechaConvertida);
				}
				
			}
			
			incidencia.setEstado(ConstantesUtils.INCIDENCIA_RESUELTA);
			incidenciaService.save(incidencia);
			empleadoService.save(empleado);
			
			log.info("Se ha resuelto correctamente la incidencia " + incidencia.toString());
			flash.addFlashAttribute("tipo", "Información");
				
		}
		
		
		model.put("incidencia", incidencia);
		model.put("titulo", "Descripción de la Incidencia");
		
		return "redirect:/incidencias/descripcionIncidencia/"+mensaje+"/"+cod_empl+"/"+fecha;
	}
	
	
	private void recuperarCamposConIncidencia(Incidencia incidencia, ArrayList<Map<String, String>> listaValoresModificados) {
		
		String descripcion = incidencia.getDescripcion();
		String campoObtenido = "";
		String valorCorrecto = "";
		
		int contadorAbertura = 0;
		int contadorCierre = 0;
		//Recuperar las incidencias de los cuatro posibles campos
		
		for(int i=0; i < descripcion.length() ; i++) {
			
			if(descripcion.charAt(i) == '(') {
				contadorAbertura ++;
			}
			if(descripcion.charAt(i) == ')') {
				contadorCierre ++;
			}
			
			/*
			 * /A partir del primer parentesis de abertura se recupera el nombre del campo 
			 * y con el segundo parentesis el valor
			 */
			if(contadorAbertura == 1 && contadorCierre == 0) {
				if(descripcion.charAt(i) != '(' && descripcion.charAt(i) != ')' ) {
					campoObtenido += descripcion.charAt(i);
				}
			}
			if(contadorAbertura == 2 && contadorCierre == 1) {
				if(descripcion.charAt(i) != '(' && descripcion.charAt(i) != ')' ) {
					valorCorrecto += descripcion.charAt(i);
				}
			}
			if(contadorAbertura == 2 && contadorCierre == 2) {
				Map<String, String> campoValor = new HashMap<String, String>();
				campoValor.put(campoObtenido, valorCorrecto);
				if(!"".equals(campoObtenido) || !"".equals(valorCorrecto)) {
					listaValoresModificados.add(campoValor);
				}
				campoObtenido = "";
				valorCorrecto = "";
			}
			
			/*
			 * Lo mismo pero para el tercer y cuarto parentesis
			 */
			if(contadorAbertura == 3 && contadorCierre == 2) {
				if(descripcion.charAt(i) != '(' && descripcion.charAt(i) != ')' ) {
					campoObtenido += descripcion.charAt(i);
				}
			}
			if(contadorAbertura == 4 && contadorCierre == 3) {
				if(descripcion.charAt(i) != '(' && descripcion.charAt(i) != ')' ) {
					valorCorrecto += descripcion.charAt(i);
				}
			}
			if(contadorAbertura == 4 && contadorCierre == 4) {
				Map<String, String> campoValor2 = new HashMap<String, String>();
				campoValor2.put(campoObtenido, valorCorrecto);
				if(!"".equals(campoObtenido) || !"".equals(valorCorrecto)) {
					listaValoresModificados.add(campoValor2);
				}
				campoObtenido = "";
				valorCorrecto = "";
			}
			
			/*
			 * Lo mismo pero para el quinto y sexto
			 */
			if(contadorAbertura == 5 && contadorCierre == 4) {
				if(descripcion.charAt(i) != '(' && descripcion.charAt(i) != ')' ) {
					campoObtenido += descripcion.charAt(i);
				}
			}
			if(contadorAbertura == 6 && contadorCierre == 5) {
				if(descripcion.charAt(i) != '(' && descripcion.charAt(i) != ')' ) {
					valorCorrecto += descripcion.charAt(i);
				}
			}
			if(contadorAbertura == 6 && contadorCierre == 6) {
				Map<String, String> campoValor3 = new HashMap<String, String>();
				campoValor3.put(campoObtenido, valorCorrecto);
				if(!"".equals(campoObtenido) || !"".equals(valorCorrecto)) {
					listaValoresModificados.add(campoValor3);
				}
				campoObtenido = "";
				valorCorrecto = "";
			}
			

			/*
			 * Lo mismo pero para el septimo y octavo
			 */
			if(contadorAbertura == 7 && contadorCierre == 6) {
				if(descripcion.charAt(i) != '(' && descripcion.charAt(i) != ')' ) {
					campoObtenido += descripcion.charAt(i);
				}
			}
			if(contadorAbertura == 8 && contadorCierre == 7) {
				if(descripcion.charAt(i) != '(' && descripcion.charAt(i) != ')' ) {
					valorCorrecto += descripcion.charAt(i);
				}
			}
			if(contadorAbertura == 8 && contadorCierre == 8) {
				Map<String, String> campoValor4 = new HashMap<String, String>();
				campoValor4.put(campoObtenido, valorCorrecto);
				if(!"".equals(campoObtenido) || !"".equals(valorCorrecto)) {
					listaValoresModificados.add(campoValor4);
				}
				campoObtenido = "";
				valorCorrecto = "";
			}
	
		}
			
	}

	private int calcularIncidenciasErrores(ArrayList<Incidencia> listaIncidencias) {
		
		int total = 0;
		for(Incidencia inci : listaIncidencias) {
			if(inci.getTipo().equals("1")) {
				total++;
			}
		}
		return total;
	}
	
	private int calcularIncidenciasAdvertencias(ArrayList<Incidencia> listaIncidencias) {
		
		int total = 0;
		for(Incidencia inci : listaIncidencias) {
			if(inci.getTipo().equals("2")) {
				total++;
			}
		}
		return total;
	}
	
	private int calcularIncidenciasSolucionadas(ArrayList<Incidencia> listaIncidencias) {
		
		int total = 0;
		for(Incidencia inci : listaIncidencias) {
			if(inci.getEstado().equals("2")) {
				total++;
			}
		}
		return total;
	}

	private int calcularIncidenciasAbiertas(ArrayList<Incidencia> listaIncidencias) {
		
		int total = 0;
		for(Incidencia inci : listaIncidencias) {
			if(inci.getEstado().equals("1")) {
				total++;
			}
		}
		return total;
	}
	
	private String recuperarFechaDescripcion(Incidencia incidencia) {
		String descripcion = incidencia.getDescripcion();
		String fechaDescripcion = "";
		int contadorAbertura = 0;
		int contadorCierre = 0;
		
		for(int i=0; i < descripcion.length() ; i++) {
			
			if(descripcion.charAt(i) == '(') {
				contadorAbertura ++;
			}
			if(descripcion.charAt(i) == ')') {
				contadorCierre ++;
			}
			//A partir del primer parentesis de abertura se recupera la fecha
			if(contadorAbertura == 1 && contadorCierre != 1) {
				fechaDescripcion += descripcion.charAt(i);
			}	
		}
		return fechaDescripcion.replaceAll("\\(", "");
	}
	
	private String recuperarHoraDescripcion(Incidencia incidencia) {
		String descripcion = incidencia.getDescripcion();
		String horaDescripcion = "";
		int contadorAbertura = 0;
		int contadorCierre = 0;
		
		for(int i=0; i < descripcion.length() ; i++) {
			
			if(descripcion.charAt(i) == '(') {
				contadorAbertura ++;
			}
			if(descripcion.charAt(i) == ')') {
				contadorCierre ++;
			}
			//A partir del segundo parentesis de abertura se recupera la fecha
			if(contadorAbertura == 2 && contadorCierre != 2) {
				horaDescripcion += descripcion.charAt(i);
			}
			
		}
		
		return horaDescripcion.replaceAll("\\(", "");
	}
	
	
}
