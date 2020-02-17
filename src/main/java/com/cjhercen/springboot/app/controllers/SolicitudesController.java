package com.cjhercen.springboot.app.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cjhercen.springboot.app.models.service.interfaces.IEmpleadoService;
import com.cjhercen.springboot.app.util.FechaUtils;

@Controller
public class SolicitudesController {
	
	@Autowired
	IEmpleadoService empleadoService;
		
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	FechaUtils fechaUtils = new FechaUtils();
	
	@RequestMapping(value = "/solicitudes")
	public String fichar(Map<String, Object> model) {

		//BORRAR, MENSAJE DE PRUEBA
		log.info("Cargando Solicitudes...");
		return "solicitudes";
	}
	
	
	
	
}
