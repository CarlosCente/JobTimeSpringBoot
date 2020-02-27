package com.cjhercen.springboot.app.controllers;

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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cjhercen.springboot.app.models.entity.Solicitud;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.object.FormSolicitud;
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
		
		//Se obtiene el usuario conectado
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		
		//Creo la solicitud con los datos obtenidos del formulario
		Solicitud solicitud = new Solicitud();
		solicitud.setTipo(obtenerTipo(formSolicitud));
		solicitud.setEmpleado(usuario.getEmpleado());
		solicitud.setFecha(formSolicitud.getFecha());
		solicitud.setEstado(SOLICITUD_ABIERTA);
		solicitud.setTiempoNecesario(0);
		
		solicitudService.save(solicitud);
		
		return "redirect:solicitudes";
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

	
	
	
}
