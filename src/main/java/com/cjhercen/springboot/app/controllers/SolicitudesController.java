package com.cjhercen.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cjhercen.springboot.app.models.entity.Empleado;
import com.cjhercen.springboot.app.models.entity.Solicitud;
import com.cjhercen.springboot.app.models.entity.Usuario;
import com.cjhercen.springboot.app.models.service.impl.SolicitudServiceImpl;
import com.cjhercen.springboot.app.models.service.impl.UsuarioServiceImpl;
import com.cjhercen.springboot.app.util.FechaUtils;
import com.cjhercen.springboot.app.util.FuncionesUtiles;

@Controller
public class SolicitudesController {
	
	@Autowired
	UsuarioServiceImpl usuarioService;
	
	@Autowired
	private SolicitudServiceImpl solicitudService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@RequestMapping(value = "/solicitudes")
	public String solicitudes(Map<String, Object> model) {

		log.debug("Entrando en solicitudes...");
		
		//Se obtienen las solicitudes del usuario si se conecta un usuario con ROLE_USER
		String username = usuarioService.getUsername();
		Usuario usuario = usuarioService.findByUsername(username);
		Empleado empleado = usuario.getEmpleado();		
		List<Solicitud> listaSolicitudesUsuario = solicitudService.findByEmpleado(empleado);
		
		//Se obtienen todas las solicitudes de todos los usuarios si se conecta un ROLE_ADMIN
		List<Solicitud> todasSolicitudes = solicitudService.findAll();
		
		
		log.debug("Saliendo de solicitudes...");

		// Introduzco los datos necesarios para pasar a la vista 
		model.put("titulo", "Solicitudes y ausencias");
		model.put("listaSolicitudesUsuario", listaSolicitudesUsuario);
		model.put("todasSolicitudes", todasSolicitudes);
		model.put("esAdmin", FuncionesUtiles.esAdmin(usuario));
		return "solicitudes";
	}
	
	
	
	
}
