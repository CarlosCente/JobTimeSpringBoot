package com.cjhercen.springboot.app.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Fichaje;
import com.cjhercen.springboot.app.models.entity.Solicitud;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.FormSolicitud;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.FichajeServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.SolicitudServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.util.ConstantesSolicitudes;
import com.cjhercen.springboot.app.util.FechaUtils;
import com.cjhercen.springboot.app.util.FuncionesUtiles;

@Controller
public class SolicitudesController implements ConstantesSolicitudes{
	
	@Autowired
	UsuarioServiceImpl usuarioService;
	
	@Autowired
	EmpleadoServiceImpl empleadoService;
	
	@Autowired
	FichajeServiceImpl fichajeService;
	
	@Autowired
	private SolicitudServiceImpl solicitudService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@RequestMapping(value = "/solicitudes")
	public String solicitudes(Map<String, Object> model) {

		log.debug("Entrando en solicitudes...");
		
		//Se obtiene el usuario conectado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		//Empleado empleado = usuario.getEmpleado();	
		
		//Si es ROLE_USER o ADMIN se pasan unos datos diferentes a la vista
		if(FuncionesUtiles.esAdmin(usuario)) {
			List<Solicitud> todasSolicitudes = solicitudService.findAll();
			model.put("todasSolicitudes", todasSolicitudes);

		} else {
			//Objeto para poder crear solicitudes.
			FormSolicitud formSolicitud = new FormSolicitud();
			model.put("formSolicitud", formSolicitud);
		}
			
		model.put("titulo", "Solicitudes y ausencias");
		model.put("esAdmin", FuncionesUtiles.esAdmin(usuario));
		return "solicitudes";
	}
	
	@RequestMapping(value = "/solicitud", method = RequestMethod.POST)
	public String solicitud(@Valid FormSolicitud formSolicitud, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			return "solicitudes";
		}
		
		//En caso de que sea una solicitud de tipo "VACACIONES" hay que controlar que se seleccionan los días
		if(obtenerTipo(formSolicitud).equals(TIPO_VACACIONES)) {
			
			Date fechaInicioVacaciones = formSolicitud.getInicioVacaciones();
			Date fechaFinVacaciones = formSolicitud.getFinVacaciones();
			Date fechaHoy = new Date();
			
			 if (fechaInicioVacaciones == null || fechaFinVacaciones == null) {
					log.info("No se han seleccionado los días en el calendario");
					flash.addFlashAttribute("tipo", "Error");
					flash.addFlashAttribute("message", "No se han seleccionado los días en el calendario");
					return "redirect:solicitudes";
			 }
 
			 if(fechaFinVacaciones.before(fechaInicioVacaciones)) {
				 log.info("La fecha fin de las vacaciones no puede ser anterior a la de inicio");
					flash.addFlashAttribute("tipo", "Error");
					flash.addFlashAttribute("message", "La fecha fin de las vacaciones no puede ser anterior a la de inicio");
					return "redirect:solicitudes";
			 }
			 
			 if(fechaInicioVacaciones.before(fechaHoy)) {
				 log.info("La fecha de inicio de las vacaciones no puede ser anterior a la fecha de hoy");
					flash.addFlashAttribute("tipo", "Error");
					flash.addFlashAttribute("message", "La fecha de inicio de las vacaciones no puede ser anterior a la fecha de hoy");
					return "redirect:solicitudes";
			 }
			 
		}
		
		if(obtenerTipo(formSolicitud).equals(TIPO_MATRIMONIO)) {
			Date fechaInicio = formSolicitud.getFecha();
			Date fechaHoy = new Date();
			
			if(formSolicitud.getFecha() == null) {
				log.info("No se ha introducido la fecha de inicio del permiso");
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "La fecha de inicio para el permiso por enfermedad o por matrimonio es obligatoria");
				return "redirect:solicitudes";
				
			}	
			
			 if(fechaInicio.before(fechaHoy)) {
				 log.info("La fecha de inicio de la solicitud no puede ser anterior a la fecha de hoy");
					flash.addFlashAttribute("tipo", "Error");
					flash.addFlashAttribute("message", "La fecha de inicio de la solicitud no puede ser anterior a la fecha de hoy");
					return "redirect:solicitudes";
			 }
			
		}
		
		if(obtenerTipo(formSolicitud).equals(TIPO_NACIMIENTO) || obtenerTipo(formSolicitud).equals(TIPO_OPERACION_FAMILIAR)){
			Date fechaInicio = formSolicitud.getFecha();
			Date fechaHoy = new Date();
			
			if (formSolicitud.getFecha() == null) {
				log.info("No se ha introducido la fecha de inicio ni si es necesario desplazamiento para el permiso");
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "La fecha de inicio y si es necesario desplazamiento son campos necesarios "
						+ "para el permiso por nacimiento de un hijo/a o por matrimonio");
				return "redirect:solicitudes";
			}
			
			 if(fechaInicio.before(fechaHoy)) {
				 log.info("La fecha de inicio de la solicitud no puede ser anterior a la fecha de hoy");
					flash.addFlashAttribute("tipo", "Error");
					flash.addFlashAttribute("message", "La fecha de inicio de la solicitud no puede ser anterior a la fecha de hoy");
					return "redirect:solicitudes";
			 }
			
		}
			
		
		//Se obtiene el usuario conectado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		
		//Creo la solicitud con los datos básicos obtenidos del formulario
		Solicitud solicitud = new Solicitud();
		solicitud.setTipo(obtenerTipo(formSolicitud));
		solicitud.setEmpleado(usuario.getEmpleado());
		solicitud.setFecha(fechaUtils.obtenerFechaActual());
		solicitud.setEstado(SOLICITUD_ABIERTA);

		//Datos faltantes según el tipo de solicitud
		//VACACIONES (DÍAS SELECCIONADOS)
		if(obtenerTipo(formSolicitud).equals(TIPO_VACACIONES)) {
			solicitud.setFechaInicioVacaciones(formSolicitud.getInicioVacaciones());
			solicitud.setFechaFinVacaciones(formSolicitud.getFinVacaciones());
			solicitud.setDiasTotales(obtenerDiasTotales(formSolicitud));
		}
		
		/*
		 * PERMISOS DE DIAS COMPLETOS
		 * Matrimonio --> Indicar día de inicio (15 días) y fecha de inicio
		 * Operación de un familiar --> Indicar si requiere desplazamiento (2 o 4 dias) y fecha de inicio
		 * Nacimiento de un hijo --> Indicar si requiere desplazamiento (2 o 4 dias) y fecha de inicio
		 */
		if(obtenerTipo(formSolicitud).equals(TIPO_NACIMIENTO) || obtenerTipo(formSolicitud).equals(TIPO_OPERACION_FAMILIAR)){
			solicitud.setNecesitaDesplazamiento(formSolicitud.getRequiereDesplazamiento());
			solicitud.setFechaInicioPermiso(formSolicitud.getFecha());
			//En el caso de nacimiento u operacion de un familiar se establecen 2 o 4 dias laborables segun el desplazamiento
			solicitud.setDiasTotales(obtenerDiasTotales(formSolicitud));
		}
		
		if(obtenerTipo(formSolicitud).equals(TIPO_MATRIMONIO)) {	
			solicitud.setFechaInicioPermiso(formSolicitud.getFecha());
			solicitud.setDiasTotales(15);
		}
		
				
		solicitudService.save(solicitud);
		log.info("La solicitud se ha creado con éxito");
		flash.addFlashAttribute("tipo", "Informacion");
		flash.addFlashAttribute("message", "La solicitud se ha creado con éxito");
		
		return "redirect:solicitudes";
	}
	
	@RequestMapping(value = "/solicitudes/aceptar")
	public String aceptarSolicitud(@RequestParam(value = "username") String username ,
			@RequestParam(value = "fecha") String fecha,
			@RequestParam(value = "tipo") String tipo) {

		if(log.isDebugEnabled()) {
			log.debug("Entrando en aceptarSolicitud()...");
		}
		
		Date fechaSolicitud = fechaUtils.obtenerFechaApartirString(fecha);
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();
		Solicitud solicitudAceptar = solicitudService.findByEmpleadoAndFechaAndTipo(empleado, fechaSolicitud, tipo);
		
		//Se marca la solicitud como aceptada
		solicitudAceptar.setEstado(SOLICITUD_ACEPTADA);
		
		//Se tratan las fechas implicadas en la solicitud segun el tipo de solicitud
		tratarSolicitudFichajes(solicitudAceptar);

		solicitudService.save(solicitudAceptar);
		
		if(log.isDebugEnabled()) {
			log.debug("Saliendo de aceptarSolicitud()... ");
		}
		
		log.info("Se ha aceptado correctamente la solicitud " + solicitudAceptar.toString());

		return "redirect:/solicitudes";
	}
	
	
	@RequestMapping(value = "/solicitudes/rechazar")
	public String rechazarSolicitud(@RequestParam(value = "username") String username ,
			@RequestParam(value = "fecha") String fecha,
			@RequestParam(value = "tipo") String tipo) {

		if(log.isDebugEnabled()) {
			log.debug("Entrando en rechazarSolicitud()...");
		}
		
		Date fechaSolicitud = fechaUtils.obtenerFechaApartirString(fecha);
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();
		Solicitud solicitudArechazar = solicitudService.findByEmpleadoAndFechaAndTipo(empleado, fechaSolicitud, tipo);
		
		solicitudArechazar.setEstado(SOLICITUD_RECHAZADA);
		
		solicitudService.save(solicitudArechazar);

		if(log.isDebugEnabled()) {
			log.debug("Saliendo de rechazarSolicitud()... ");
		}
		
		log.info("Se ha rechazado correctamente la solicitud " + solicitudArechazar.toString());

		return "redirect:/solicitudes";
	}
	
	@RequestMapping(value = "/solicitudes/eliminar")
	public String eliminarSolicitud(@RequestParam(value = "username") String username ,
			@RequestParam(value = "fecha") String fecha,
			@RequestParam(value = "tipo") String tipo) {

		if(log.isDebugEnabled()) {
			log.debug("Entrando en eliminarSolicitud()...");
		}
		
		Date fechaSolicitud = fechaUtils.obtenerFechaApartirString(fecha);
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();
		Solicitud solicitudABorrar = solicitudService.findByEmpleadoAndFechaAndTipo(empleado, fechaSolicitud, tipo);
		
		solicitudService.delete(solicitudABorrar);

		if(log.isDebugEnabled()) {
			log.debug("Saliendo de eliminarSolicitud()... ");
		}
		
		log.info("Se ha borrado correctamente la solicitud " + solicitudABorrar.toString());

		return "redirect:/solicitudes";
	}
	
	//Metodo que devuelve el tipo de la solicitud elegida en el formulario
	private String obtenerTipo(@Valid FormSolicitud formSolicitud) {
		String tipo = "";
		
		switch (formSolicitud.getTipo()) {
		case "1": 
			tipo = TIPO_VACACIONES;
			break;
		case "2":
			tipo = TIPO_OPERACION_FAMILIAR;
			break;
		case "3":
			tipo = TIPO_MATRIMONIO;
			break;
		case "4":
			tipo = TIPO_NACIMIENTO;
			break;
		default:
			tipo = "";
			break;
		}
		
		return tipo;
	}

	//Metodo que devuelve los días totales de la solicitud 
	private int obtenerDiasTotales(FormSolicitud solicitud) {
		int retorno = 0;
		
		//NACIMIENTO DE UN HIJO Y OPERACION DE UN FAMILIAR
		if(solicitud.getTipo().equals("2") || solicitud.getTipo().equals("4")) {
			if("Si".equals(solicitud.getRequiereDesplazamiento())){
				retorno = 4;
			} else {
				retorno = 2;
			}
			
		//VACACIONES
		} else if (solicitud.getTipo().equals("1")) {
			//Recoger fecha de inicio y fecha de fin para poder sacar los dias que hay entre ellos,
			//para ello tendría que restar los fines de semana ya que solo se contarán los días laborales
			//O puedo cogerlos todos e ir apuntando los dias que se han seleccionado, todos sin excepcion
			Date fechaInicio = solicitud.getInicioVacaciones();
			Date fechaFin = solicitud.getFinVacaciones();

			int dias=(int) ((fechaFin.getTime()-fechaInicio.getTime())/86400000);
			retorno = dias+1;
		}
		
		return retorno;
	}
	
	/*
	 * Método que introduce los fichajes necesarios en base de datos que se ven ligados a la solicitud 
	 */
	private void tratarSolicitudFichajes(Solicitud solicitudAceptar) {
				
		String tipo = solicitudAceptar.getTipo();
		//Casos Nacimiento de un hijo y Operacion de un familiar, dispone de los días necesarios que pueden ser 2 o 4
		if(tipo.equals(TIPO_NACIMIENTO) || tipo.equals(TIPO_OPERACION_FAMILIAR)) {
			
			//Obtengo los dias totales del permiso y desde cuando se inicia el mismo para crear los fichajes necesarios
			int totalDias = solicitudAceptar.getDiasTotales();
			Date fechaInicioPermiso = solicitudAceptar.getFechaInicioPermiso();
			log.debug("Vamos a entrar en while para tratar la solicitud de nacimiento u de operacion de un familiar,"
					+ " dias totales: " + totalDias);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaInicioPermiso);
			Date fechaContador = calendar.getTime();
			while(totalDias >0) {
				//Creo los fichajes necesarios para rellenar los dias
				Empleado empleado = solicitudAceptar.getEmpleado();
				Fichaje fichajeConPermiso = new Fichaje();
						
				fichajeConPermiso.setEmpleado(empleado);
				fichajeConPermiso.setFecha(fechaContador);
				fichajeConPermiso.setIp("Creado por admin");
				fichajeConPermiso.setFinalizado(true);
				fichajeConPermiso.setTiempoTotal("08:00");
				fichajeConPermiso.setSemanaDelAnnio(fechaUtils.obtenerSemana(fechaContador));
				fichajeConPermiso.setTienePermiso(true);
				fichajeConPermiso.setTipoPermiso(solicitudAceptar.getTipo());
						
				if(!fechaUtils.esFinDeSemana(fechaContador)){
					totalDias--;
					fichajeService.save(fichajeConPermiso);
				}
				
				fechaContador = fechaUtils.sumarDias(fechaContador, 1);
			}//Fin while
			
		} else if(tipo.equals(TIPO_MATRIMONIO)) {
			//Obtengo los dias totales del permiso y desde cuando se inicia el mismo para crear los fichajes necesarios
			int totalDias = solicitudAceptar.getDiasTotales();
			Date fechaInicioPermiso = solicitudAceptar.getFechaInicioPermiso();
			log.debug("Vamos a entrar en while para tratar la solicitud de matrimonio, dias totales: " + totalDias);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaInicioPermiso);
			Date fechaContador = calendar.getTime();
			while(totalDias >0) {
				//Creo los fichajes necesarios para rellenar los dias
				Empleado empleado = solicitudAceptar.getEmpleado();
				Fichaje fichajeConPermiso = new Fichaje();
						
				fichajeConPermiso.setEmpleado(empleado);
				fichajeConPermiso.setFecha(fechaContador);
				fichajeConPermiso.setIp("Creado por admin");
				fichajeConPermiso.setFinalizado(true);
				fichajeConPermiso.setTiempoTotal("08:00");
				fichajeConPermiso.setSemanaDelAnnio(fechaUtils.obtenerSemana(fechaContador));
				fichajeConPermiso.setTienePermiso(true);
				fichajeConPermiso.setTipoPermiso(solicitudAceptar.getTipo());
					
				//En este caso serán 15 dias naturales por lo que no se mira si es o no fin de semana
				totalDias--;
				
				//Se controla para que no se creen fichajes los sabados ni los domingos
				if(!fechaUtils.esFinDeSemana(fechaContador)) {
					fichajeService.save(fichajeConPermiso);
				}
				
				fechaContador = fechaUtils.sumarDias(fechaContador, 1);
			}//Fin while
		
		} else if(tipo.equals(TIPO_VACACIONES)) {
			//Obtengo los dias totales del permiso y desde cuando se inicia el mismo para crear los fichajes necesarios
			int totalDias = solicitudAceptar.getDiasTotales();
			Date fechaInicioVacaciones = solicitudAceptar.getFechaInicioVacaciones();
			log.debug("Vamos a entrar en while para tratar la solicitud de matrimonio, dias totales: " + totalDias);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaInicioVacaciones);
			Date fechaContador = calendar.getTime();
			while(totalDias >0) {
				//Creo los fichajes necesarios para rellenar los dias
				Empleado empleado = solicitudAceptar.getEmpleado();
				Fichaje fichajeConPermiso = new Fichaje();
						
				fichajeConPermiso.setEmpleado(empleado);
				fichajeConPermiso.setFecha(fechaContador);
				fichajeConPermiso.setIp("Creado por admin");
				fichajeConPermiso.setFinalizado(true);
				fichajeConPermiso.setTiempoTotal("08:00");
				fichajeConPermiso.setSemanaDelAnnio(fechaUtils.obtenerSemana(fechaContador));
				fichajeConPermiso.setTienePermiso(true);
				fichajeConPermiso.setTipoPermiso(solicitudAceptar.getTipo());
					
				//En este caso serán 15 dias naturales por lo que no se mira si es o no fin de semana
				totalDias--;
				
				//Se controla para que no se creen fichajes los sabados ni los domingos
				if(!fechaUtils.esFinDeSemana(fechaContador)) {
					fichajeService.save(fichajeConPermiso);
				}
				
				fechaContador = fechaUtils.sumarDias(fechaContador, 1);
			}//Fin while
		}
		
	}
	
}
