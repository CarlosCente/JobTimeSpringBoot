package com.cjhercen.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cjhercen.springboot.app.models.entity.Incidencia;
import com.cjhercen.springboot.app.models.service.interfaces.IIncidenciaService;

@Controller
public class IncidenciaController {

	@Autowired
	IIncidenciaService incidenciaService;
	
	@RequestMapping(value = "/incidencias")
	public String crear(Map<String, Object> model) {

		List<Incidencia> incidenciasPresentes = incidenciaService.findAll();
		
		model.put("incidencias", incidenciasPresentes);
		model.put("titulo", "Incidencias de los empleados");
		return "incidencias";
	}
	
}
