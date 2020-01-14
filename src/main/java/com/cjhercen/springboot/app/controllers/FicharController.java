package com.cjhercen.springboot.app.controllers;

import java.text.DecimalFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.IncidenciaFichaje;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.FichajeServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.IncidenciaServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.util.ConstantesUtils;
import com.cjhercen.springboot.app.util.FechaUtils;
import com.cjhercen.springboot.app.util.FuncionesUtiles;

@Controller
public class FicharController implements ConstantesUtils {

	@Autowired
	EmpleadoServiceImpl empleadoService;
	
	@Autowired
	UsuarioServiceImpl usuarioService;

	@Autowired
	private IncidenciaServiceImpl incidenciaService;
	
	@Autowired
	FichajeServiceImpl fichajeService;
		
	FechaUtils fechaUtils = new FechaUtils();
	
	String horaActual = fechaUtils.obtenerHoraEnFormatoCadena();
	
	@RequestMapping(value = "/fichar")
	public String fichar(Map<String, Object> model,  HttpServletRequest request) {

		//Introduzco el objeto en el modelo para poder crear incidencias
		IncidenciaFichaje incidenciaFichaje = new IncidenciaFichaje();
		model.put("incidenciaFichaje", incidenciaFichaje);
		
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();		
				
		//Comprobación si ya se ha hecho el fichaje de entrada o de salida
		Boolean hayEntrada = false;
		Boolean haySalida = false;
		String porcentaje = "0%";
		
		//Datos del fichaje (Hora entrada y hora de salida)
		Fichaje fichajeEmpleado = fichajeService.findByEmpleadoAndFecha(empleado, fechaUtils.obtenerFechaActual());
		if(fichajeEmpleado != null) {
			if(fichajeEmpleado.getHoraEntrada() == null | "".equals(fichajeEmpleado.getHoraEntrada())) {
				model.put("horaEntrada", "-");
			} else {
				model.put("horaEntrada", fichajeEmpleado.getHoraEntrada());
				hayEntrada = true;
			}
			
			if(fichajeEmpleado.getHoraSalida() == null | "".equals(fichajeEmpleado.getHoraSalida())) {
				model.put("horaSalida", "-");
			} else {
				model.put("horaSalida", fichajeEmpleado.getHoraSalida());
				haySalida = true;
			}			
			
			//Cálculo del tiempo transcurrido del fichaje
			String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichajeEmpleado.getHoraEntrada(), fichajeEmpleado.getHoraSalida());
			model.put("totalTiempo" , fechaUtils.formatearFechas2digitos(totalTiempo));
			porcentaje = calcularPorcentajeFichado(totalTiempo);

			
		} else {
			model.put("horaEntrada", "-");
			model.put("horaSalida", "-");
			model.put("totalTiempo", "00:00");
		}
		
		if("-".equals(porcentaje)) {
			porcentaje = "0%";
		}
		model.put("porcentaje", porcentaje);
		model.put("fechaHora", fechaUtils.obtenerFechaEnFormatoCadena() + " " + horaActual);
		model.put("horaActual", horaActual);
		model.put("titulo", "Fichaje del Empleado");
		model.put("empleado", empleado);
		model.put("ip_cliente", FuncionesUtiles.obtenerIp(request));
		model.put("esAdmin", FuncionesUtiles.esAdmin(usuario));
		model.put("hayEntrada", hayEntrada);
		model.put("haySalida", haySalida);
		
		return "fichar";
	}
	
	
	@RequestMapping(value = "/fichar/entrada/")
	public String ficharEntrada(RedirectAttributes flash, HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();

		Fichaje fichajeComprueba = fichajeService.findByEmpleadoAndFecha(empleado, fechaUtils.obtenerFechaActual());
		
		if(fichajeComprueba != null) {
			flash.addFlashAttribute("error", "Ya has realizado el fichaje de entrada hoy!");
		} else {
			Fichaje fichajeEntrada = new Fichaje();
			fichajeEntrada.setEmpleado(empleado);
			fichajeEntrada.setFecha(fechaUtils.obtenerFechaActual());
			fichajeEntrada.setHoraEntrada(fechaUtils.obtenerHoraEnFormatoCadena());
			fichajeEntrada.setIp(request.getRemoteAddr());
			
			fichajeService.save(fichajeEntrada);	
			
			flash.addFlashAttribute("success", "Has fichado la entrada correctamente a las " + fechaUtils.obtenerHoraEnFormatoCadena());	
		}
		
		return "redirect:/fichar";
	}
		
	
	@RequestMapping(value = "/fichar/salida/")
	public String ficharSalida(RedirectAttributes flash, HttpServletRequest request) {

		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();

		//Se comprueba si existe el fichaje de entrada, sino devolvería error
		Fichaje fichajeComprueba = fichajeService.findByEmpleadoAndFecha(empleado, fechaUtils.obtenerFechaActual());
		
		if(fichajeComprueba == null) {
			flash.addFlashAttribute("error", "No has fichado aún la entrada de hoy");
		} else {
			
			if(fichajeComprueba.getHoraSalida() != null && !"".equals(fichajeComprueba.getHoraSalida())){
				
				flash.addFlashAttribute("error", "Ya has realizado el fichaje de salida hoy");	
				
			} else {
				//Una vez que se han realizado los dos fichajes, se guarda el tiempo total en la BBDD y marcamos el fichaje
				//de dia como finalizado.
				
				String totalTiempo = fechaUtils.obtenerTiempoTranscurrido(fichajeComprueba.getHoraEntrada(), fichajeComprueba.getHoraSalida());
				fichajeComprueba.setHoraSalida(fechaUtils.obtenerHoraEnFormatoCadena());
				fichajeComprueba.setIp(FuncionesUtiles.obtenerIp(request));
				fichajeComprueba.setTiempoTotal(fechaUtils.formatearFechas2digitos(totalTiempo));
				fichajeComprueba.setFinalizado(true);
				
				fichajeService.save(fichajeComprueba);	
				
				flash.addFlashAttribute("success", "Has fichado la salida correctamente a las " + fechaUtils.obtenerHoraEnFormatoCadena());	
			}
	
		}
		
		return "redirect:/fichar";
	}
	
	
	@RequestMapping(value = "/fichar/incidencia", method = RequestMethod.POST)
	public String saveIncidencia(@ModelAttribute("incidenciaFichaje") IncidenciaFichaje incidenciaFichaje,
			BindingResult bindingResult, RedirectAttributes flash) {
	
		/*
		 * Se controla que no vaya la incidencia completamente vacía
		 */
		if(INCIDENCIA_NO_SELECCION.equals(incidenciaFichaje.getTipo())){
			flash.addFlashAttribute("error", "Error, no se puede crear una incidencia vacía");
			return "redirect:/fichar";
		}
		
		Empleado empleadoBD = null;
		
		//Se obtiene primero el usuario conectado para obtener los datos del empleado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		empleadoBD = usuario.getEmpleado();		

		if (empleadoBD != null) {
	
		/*
		 * Si no ocurre ningún error con el empleado...
		 * Se crea la incidencia con estado abierta y con tipo ERROR para el empleado que se encuentra conectado
		 */
		
			Incidencia incidenciaNueva = new Incidencia();
			incidenciaNueva.setEmpleado(empleadoBD);
			incidenciaNueva.setTipo(INCIDENCIA_ERROR);
			incidenciaNueva.setEstado(INCIDENCIA_ABIERTA);
			incidenciaNueva.setFecha(fechaUtils.obtenerFechaActual());
			
			//Se establece el mensaje (el tipo de incidencia que es)
			if(INCIDENCIA_ENTRADA.equals(incidenciaFichaje.getTipo())) {
				incidenciaNueva.setMensaje(INCIDENCIA_FICHAJE_ENTRADA);
			} else if (INCIDENCIA_SALIDA.equals(incidenciaFichaje.getTipo())) {
				incidenciaNueva.setMensaje(INCIDENCIA_FICHAJE_SALIDA);
			} else if (INCIDENCIA_OTROS.equals(incidenciaFichaje.getTipo())) {
				incidenciaNueva.setMensaje(INCIDENCIA_FICHAJE_OTROS);
			}
			
			//Se establece la descripcion dependiendo tambien del tipo de incidencia
			incidenciaNueva.setDescripcion(generarDescripcion(empleadoBD, incidenciaFichaje));
					
			incidenciaService.save(incidenciaNueva);

			flash.addFlashAttribute("success", "Incidencia añadida correctamente");

		} else {
			flash.addFlashAttribute("error", "Error, no se ha podido crear la incidencia");
		}
		
		return "redirect:/fichar";
	}
	
	public String calcularPorcentajeFichado(String tiempoTotal) {
		DecimalFormat df = new DecimalFormat("#");
		int totalTiempo = fechaUtils.obtenerHoraEnMinutos(tiempoTotal);
		String value = "0";
		double totalJornada = 480;
		double porcentajeInt;
		
		if(totalTiempo > 0) {
			porcentajeInt = totalTiempo / totalJornada*100;
			value = df.format(porcentajeInt);
		} 
		
		return value +'%';
	}
	
	/**
	 * Método que genera la descripción de la incidencia según del tipo que sea y de los datos
	 * @param empleado empleado que crea la incidencia (usuario conectado)
	 * @param incidenciaFichaje datos pasados en el modal
	 * @return String con la descripcion de la incidencia
	 */
	public String generarDescripcion(Empleado empleado, IncidenciaFichaje incidenciaFichaje) {
		
		String mensaje = "";
				
		if(INCIDENCIA_ENTRADA.equals(incidenciaFichaje.getTipo())) {
			
			String fechaHoraEntrada = incidenciaFichaje.getFechaHoraEntrada();
			String comentarioEntrada = incidenciaFichaje.getComentarioEntrada();
			
			String [] partesEntrada = fechaHoraEntrada.split(" ");
			String fechaEntrada = partesEntrada[0];
			String horaEntrada = partesEntrada[1];
			
			mensaje = "El usuario "+empleado.getUsuario().getUsername() + " (" + empleado.getNombre() + " " +
		empleado.getApellido1() + " " + empleado.getApellido2() +"), ha creado una incidencia con su fichaje de entrada del día "
				+ fechaEntrada +" la hora correcta sería a las "+ horaEntrada +". Además el usuario ha añadido el siguiente comentario:"
						+ " "+comentarioEntrada;
		} 
		
		if(INCIDENCIA_SALIDA.equals(incidenciaFichaje.getTipo())) {
			String fechaHoraSalida = incidenciaFichaje.getFechaHoraSalida();
			String comentarioSalida = incidenciaFichaje.getComentarioSalida();
			
			String [] partesSalida = fechaHoraSalida.split(" ");
			String fechaSalida = partesSalida[0];
			String horaSalida = partesSalida[1];
			
			mensaje = "El usuario "+empleado.getUsuario().getUsername() + " (" + empleado.getNombre() + " " +
					empleado.getApellido1() + " " + empleado.getApellido2() +"), ha creado una incidencia con su fichaje de salida del día "
					+ fechaSalida + " la hora correcta sería a las "+ horaSalida + ". Además el usuario ha añadido el siguiente comentario:"
					+ " " + comentarioSalida;
		} 

		if(INCIDENCIA_OTROS.equals(incidenciaFichaje.getTipo())) {
			String comentarioOtro = incidenciaFichaje.getComentarioOtro();
			
			mensaje = "El usuario "+empleado.getUsuario().getUsername() + " (" + empleado.getNombre() + " " +
					empleado.getApellido1() + " " + empleado.getApellido2() +"), ha creado una incidencia con su fichaje y ha dejado el"
							+ " siguiente comentario: " + comentarioOtro;

		} 
		
		return mensaje;
	}
	
	
}
