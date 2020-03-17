package com.cjhercen.springboot.app.controllers;

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
import com.cjhercen.springboot.app.models.entity.Solicitud;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.FormSolicitud;
import com.cjhercen.springboot.app.models.service.impl.EmpleadoServiceImpl;
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
			 if (formSolicitud.getInicioVacaciones() == null || formSolicitud.getFinVacaciones() == null) {
					log.info("No se han seleccionado los días en el calendario");
					flash.addFlashAttribute("tipo", "Error");
					flash.addFlashAttribute("message", "No se han seleccionado los días en el calendario");
					return "redirect:solicitudes";
			 }
		}
		
		if(obtenerTipo(formSolicitud).equals(TIPO_ENFERMEDAD) || obtenerTipo(formSolicitud).equals(TIPO_MATRIMONIO)) {	
			if(formSolicitud.getFecha() == null) {
				log.info("No se ha introducido la fecha de inicio del permiso");
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "La fecha de inicio para el permiso por enfermedad o por matrimonio es obligatoria");
				return "redirect:solicitudes";
				
			}	
		}
		
		if(obtenerTipo(formSolicitud).equals(TIPO_NACIMIENTO) || obtenerTipo(formSolicitud).equals(TIPO_OPERACION_FAMILIAR)){
			if (formSolicitud.getFecha() == null) {
				log.info("No se ha introducido la fecha de inicio ni si es necesario desplazamiento para el permiso");
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "La fecha de inicio y si es necesario desplazamiento son campos necesarios "
						+ "para el permiso por nacimiento de un hijo/a o por matrimonio");
				return "redirect:solicitudes";
			}
			
		}
		
		if(obtenerTipo(formSolicitud).equals(TIPO_EXAMEN) || obtenerTipo(formSolicitud).equals(TIPO_FORMACION) ||
				obtenerTipo(formSolicitud).equals(TIPO_CITA_MEDICA)) {	
			if(formSolicitud.getFecha() == null || formSolicitud.getTiempoNecesario() <= 0 || formSolicitud.getTiempoNecesario() > 8) {
				log.info("No se ha introducido la fecha de inicio o las horas necesarias para el permiso");
				flash.addFlashAttribute("tipo", "Error");
				flash.addFlashAttribute("message", "Los campos de fecha de inicio y las horas necesarias son necesarias para"
						+ " los permisos de examen, cita médica o asistencia a formación");
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
		}
		
		/*
		 * PERMISOS DE DIAS COMPLETOS
		 * Enfermedad --> Hasta que se coja el alta, indicar fecha de inicio
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
		
		if(obtenerTipo(formSolicitud).equals(TIPO_ENFERMEDAD) || obtenerTipo(formSolicitud).equals(TIPO_MATRIMONIO)) {	
			solicitud.setFechaInicioPermiso(formSolicitud.getFecha());
			//En el caso de matrimonio se establecen los 15 dias naturales
			if(obtenerTipo(formSolicitud).equals(TIPO_MATRIMONIO)) {
				solicitud.setDiasTotales(obtenerDiasTotales(formSolicitud));
			}
		}
		
		
		/*
		 * PERMISOS DE HORAS O PARCIALES
		 * Examen --> Indicar horas necesarias y el dia de inicio
		 * Cita médica --> Indicar horas necesarias y el dia de inicio
		 * Asistencia a formación --> Indicar horas necesarias y el dia de inicio
		 */
		if(obtenerTipo(formSolicitud).equals(TIPO_EXAMEN) || obtenerTipo(formSolicitud).equals(TIPO_FORMACION) ||
				obtenerTipo(formSolicitud).equals(TIPO_CITA_MEDICA)) {	
			solicitud.setTiempoNecesario(formSolicitud.getTiempoNecesario());
			solicitud.setFechaInicioPermiso(formSolicitud.getFecha());
		}
		
		
		solicitudService.save(solicitud);
		log.info("La solicitud se ha creado con éxito");
		flash.addFlashAttribute("tipo", "Informacion");
		flash.addFlashAttribute("message", "La solicitud se ha creado con éxito");
		
		return "redirect:solicitudes";
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
			tipo = TIPO_ENFERMEDAD;
			break;
		case "3":
			tipo = TIPO_OPERACION_FAMILIAR;
			break;
		case "4":
			tipo = TIPO_MATRIMONIO;
			break;
		case "5":
			tipo = TIPO_NACIMIENTO;
			break;
		case "6":
			tipo = TIPO_EXAMEN;
			break;
		case "7":
			tipo = TIPO_CITA_MEDICA;
			break;
		case "8":
			tipo = TIPO_FORMACION;
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
		
		return retorno;
	}
	
}
